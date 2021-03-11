package com.iwhalecloud.lottery.service;

import com.iwhalecloud.lottery.config.LotteryParam;
import com.iwhalecloud.lottery.exception.LtException;
import com.iwhalecloud.lottery.mapper.MobileLotteryMapper;
import com.iwhalecloud.lottery.pojo.MobileLotteryDo;
import com.iwhalecloud.lottery.redis.RedisCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: ParamService</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-26 12:39
 */
@Slf4j
@Service
public class ParamService {

    /**
     * 获奖人数读写锁
     */
    public static final String WIN_RW_LOCK = "rw-lock";
    /**
     * 等待;租期 默认为10s
     */
    public static final long TRY_LOCK_TIME = 10L;
    private final Object initFirstDayLock = new Object();
    private RedisCacheManager redisCacheManager;
    private RedissonClient redissonClient;

    @Resource
    private MobileLotteryMapper mobileLotteryMapper;

    @Autowired
    public ParamService(RedisCacheManager redisCacheManager, RedissonClient redissonClient) {
        this.redisCacheManager = redisCacheManager;
        this.redissonClient = redissonClient;
    }

    /**
     * 查询记录
     *
     * @return 记录
     */
    public List<MobileLotteryDo> queryResult() {
        return mobileLotteryMapper.query();
    }

    /**
     * 获取指定key的值.
     *
     * @param key key
     * @return value
     */
    public Object getValueByKey(String key) {
        return redisCacheManager.get(key);
    }

    /**
     * 初始化首日业务参数
     *
     * @param baseWinCount 获奖基数
     * @param maxWinCount  最大获奖数
     * @param predictCount 预测获奖数
     */
    public void initParam(int baseWinCount, int maxWinCount, int predictCount, boolean isFirst) {
        synchronized (initFirstDayLock) {
            // 1. 设置当天获奖基数
            init(LotteryParam.REDIS_KEY_BASE_WIN_NUM, baseWinCount);
            // 2. 设置最大基数
            init(LotteryParam.REDIS_KEY_MAX_WIN_NUM, maxWinCount);
            // 3. 设置当天预期抽奖次数
            init(LotteryParam.REDIS_KEY_PREDICT_NUM, predictCount);
            // 4. 初始化中奖人数:0
            init(LotteryParam.REDIS_KEY_WIN_PEOPLE_NUM, 0);
            init(LotteryParam.REDIS_KEY_PEOPLE_NUM, 0);
            if (Boolean.TRUE.equals(isFirst)) {
                init(LotteryParam.REDIS_ROUND, 1);
                mobileLotteryMapper.oldDataHandle();
            }
            else {
                // round 轮数自增
                roundIncr();
            }
            redisCacheManager.pushParamUpdateMsg();
        }
    }

    /**
     * 查询业务参数
     *
     * @return 业务参数json
     */
    public MobileLotteryDo queryParam() {
        MobileLotteryDo mobileLotteryDo = new MobileLotteryDo();
        try {
            Integer baseWinNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_KEY_BASE_WIN_NUM).toString());
            Integer maxWinNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_KEY_MAX_WIN_NUM).toString());
            Integer peopleNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_KEY_PEOPLE_NUM).toString());
            Integer predictNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_KEY_PREDICT_NUM).toString());
            Integer winPeopleNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_KEY_WIN_PEOPLE_NUM).toString());
            Integer roundNum = Integer.valueOf(redisCacheManager.get(LotteryParam.REDIS_ROUND).toString());
            mobileLotteryDo.setBaseWinNum(baseWinNum);
            mobileLotteryDo.setMaxWinNum(maxWinNum);
            mobileLotteryDo.setPeopleNum(peopleNum);
            mobileLotteryDo.setPredictNum(predictNum);
            mobileLotteryDo.setWinPeopleNum(winPeopleNum);
            mobileLotteryDo.setRound(roundNum);
        }
        catch (Exception e) {
            log.error("redis数据异常: {}", e.getMessage());
        }
        return mobileLotteryDo;
    }


    private void init(String key, Object value) {
        boolean state = redisCacheManager.set(key, String.valueOf(value));
        if (state) {
            log.info("init {}: {}", key, value.toString());
        }
        else {
            log.info("init {} fail", key);
        }
    }

    /**
     * 自增.
     *
     * @param key key
     * @return value
     */
    public Long incr(String key) {
        return redisCacheManager.incr(key, 1L);
    }

    /**
     * 保存当前轮结果, 并初始下一轮参数
     */
    public void nextRound() {
        RLock lock = redissonClient.getLock(ParamService.WIN_RW_LOCK);
        try {
            if (lock.tryLock(TRY_LOCK_TIME, TRY_LOCK_TIME, TimeUnit.SECONDS)) {
                log.info("#nextRound >>>>>>>>> {} acquired", ParamService.WIN_RW_LOCK);
                // 取出redis中的结果值,并保存
                MobileLotteryDo mobileLotteryDo = queryParam();
                int insertNum = mobileLotteryMapper.insert(mobileLotteryDo);
                if (insertNum == 1) {
                    log.info("成功保存当前第{}轮结果", mobileLotteryDo.getRound());
                    // 参数更新.
                    Integer baseWinNum = mobileLotteryDo.getBaseWinNum();
                    Integer winPeopleNum = mobileLotteryDo.getWinPeopleNum();
                    int maxValue = baseWinNum;
                    if (baseWinNum > winPeopleNum) {
                        maxValue = baseWinNum + (baseWinNum - winPeopleNum);
                    }
                    // 1. 基数固定.
                    // 2. 最大获奖 = 上一轮剩余 + 基数.
                    // 3. 预测抽奖人数 = 上一轮实际抽奖人数.
                    initParam(baseWinNum, maxValue, mobileLotteryDo.getPeopleNum(), false);
                    log.info("下一轮参数初始化成功");
                }
                else {
                    log.error("未成功保存当前第{}轮结果!!!", mobileLotteryDo.getRound());
                    throw new LtException("参数保存失败");
                }
            }
            else {
                log.info("{} lock acquired fail", ParamService.WIN_RW_LOCK);
            }
        }
        catch (InterruptedException e) {
            // 记录中断
            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            log.error("#nextRound 处理异常:{}", e.getMessage(), e);
            throw new LtException("系统异常");
        }
        finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("#winHandle <<<<<<<< {} release", ParamService.WIN_RW_LOCK);
            }
        }
    }

    /**
     * 自增回合轮数
     */
    private void roundIncr() {
        // 轮数 + 1
        try {
            incr(LotteryParam.REDIS_ROUND);
        }
        catch (Exception e) {
            log.error("自增轮数异常: {}", e.getMessage(), e);
            throw new LtException("自增轮数失败");
        }
    }
}
