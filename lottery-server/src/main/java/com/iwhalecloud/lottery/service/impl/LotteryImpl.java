package com.iwhalecloud.lottery.service.impl;


import com.iwhalecloud.lottery.config.LotteryParam;
import com.iwhalecloud.lottery.exception.LtException;
import com.iwhalecloud.lottery.service.LotteryService;
import com.iwhalecloud.lottery.service.ParamService;
import com.iwhalecloud.lottery.util.LotteryUtil;
import com.iwhalecloud.lottery.util.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: LotteryImpl</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-26 20:20
 */
@Slf4j
@Service
public class LotteryImpl implements LotteryService {

    @Resource
    ParamService paramService;

    private static Integer maxWinNum;
    private static double rate;
    private RedissonClient redissonClient;
    private ThreadPoolManager threadPoolManager;

    @Autowired
    public LotteryImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        this.threadPoolManager = ThreadPoolManager.getInstance();
    }

    @Override
    public int luckyDraw() {
        try {
            // 全局操作: 抽奖人数++ 异步处理
            threadPoolManager.execute(() -> paramService.incr(LotteryParam.REDIS_KEY_PEOPLE_NUM));
            // ----------------未中奖---------------
            // 抽奖
            int lottery = LotteryUtil.lottery(rate);
            if (lottery == LotteryParam.WIN_FALSE) {
                log.info("未中奖");
                return lottery;
            }
            // ----------------中奖---------------------
            // 1. 写锁 中奖人数 + 1
            boolean b = winHandle();
            if (!b) {
                return LotteryParam.WIN_FALSE;
            }
        }
        catch (Exception e) {
            log.error("---------{}--------", e.getMessage(), e);
            throw new LtException("luckyDraw 异常");
        }
        return LotteryParam.WIN_TRUE;
    }

    private boolean winHandle() {
        RLock lock = redissonClient.getLock(ParamService.WIN_RW_LOCK);
        try {
            // 等待3s, 上锁后10s租期
            if (lock.tryLock(ParamService.TRY_LOCK_TIME, ParamService.TRY_LOCK_TIME, TimeUnit.SECONDS)) {
                log.info("#winHandle >>>>>>> {} acquired", ParamService.WIN_RW_LOCK);
                String winNumStr = paramService.getValueByKey(LotteryParam.REDIS_KEY_WIN_PEOPLE_NUM).toString();
                int winPeopleNum = Integer.parseInt(winNumStr);
                if (winPeopleNum >= maxWinNum) {
                    log.info("0-当前win人数:{}", winNumStr);
                    return false;
                }
                else {
                    paramService.incr(LotteryParam.REDIS_KEY_WIN_PEOPLE_NUM);
                    log.info("1-当前win人数:{}", Integer.valueOf(winNumStr) + 1);
                }
            }
            else {
                log.info("######### winHandle {} acquired fail ##################", ParamService.WIN_RW_LOCK);
                return false;
            }
        }
        catch (InterruptedException e) {
            // 记录中断
            Thread.currentThread().interrupt();
        }
        catch (Exception e) {
            log.error("#winHandle处理异常:{}", e.getMessage(), e);
            throw new LtException("系统异常");
        }
        finally {
            //解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("#winHandle <<<<<<<< {} release", ParamService.WIN_RW_LOCK);
            }
        }
        return true;
    }

    public static void setMaxWinNum(Integer maxWinNum) {
        LotteryImpl.maxWinNum = maxWinNum;
    }

    public static void setRate(double rate) {
        LotteryImpl.rate = rate;
    }
}
