package com.iwhalecloud.lottery.config;

import com.iwhalecloud.lottery.redis.RedisCacheManager;
import com.iwhalecloud.lottery.service.impl.LotteryImpl;
import com.iwhalecloud.lottery.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>Title: LotteryCache</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-22 17:23
 */
@Slf4j
@Component
public class LotteryStartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        RedisCacheManager redisCacheManager = SpringUtil.getBean(RedisCacheManager.class);
        initLotteryParam(redisCacheManager);
    }

    /**
     * 初始化/更新内存中maxWinNum;predictNum值
     *
     * @param redisCacheManager redisCacheManager
     */
    public static void initLotteryParam(RedisCacheManager redisCacheManager) {
        try {
            String redisMax = redisCacheManager.get(LotteryParam.REDIS_KEY_MAX_WIN_NUM).toString();
            String predictNum = redisCacheManager.get(LotteryParam.REDIS_KEY_PREDICT_NUM).toString();
            Integer max;
            if (!StringUtils.isEmpty(redisMax)) {
                max = Integer.valueOf(redisMax);
                log.info("加载redis中 maxWinNum 参数值: {}", max);
            }
            else {
                log.info("加载redis中 maxWinNum 参数为空, 初始化该值为0");
                max = 0;
            }
            LotteryImpl.setMaxWinNum(max);

            double rate = 0.0;
            if (!StringUtils.isEmpty(predictNum)) {
                log.info("加载redis中predictNum 参数值: {}", predictNum);
                Double maxDouble = Double.valueOf(redisMax);
                Double predictNumDouble = Double.valueOf(predictNum);
                rate = maxDouble / predictNumDouble;
                log.info("轮获奖概率: {}", rate);
            }
            else {
                log.info("加载redis中predictNum参数为空, 初始化本轮概率为0");
            }
            LotteryImpl.setRate(rate);


        }
        catch (Exception e) {
            log.info("加载redis中 maxWinNum 参数值异常: {}", e.getMessage());
            LotteryImpl.setMaxWinNum(0);
        }
    }

}
