package com.iwhalecloud.lottery.pojo;

import com.iwhalecloud.lottery.config.LotteryStartupRunner;
import com.iwhalecloud.lottery.redis.RedisCacheManager;
import com.iwhalecloud.lottery.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>Title: MessageReceive</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-22 20:03
 */
@Component
@Slf4j
public class MessageReceive {

    /**
     * 接收消息的方法
     */
    public void receiveMessage(String message) {
        log.info("收到参数更新消息:{}...", message);
        RedisCacheManager redisCacheManager = SpringUtil.getBean(RedisCacheManager.class);
        LotteryStartupRunner.initLotteryParam(redisCacheManager);

    }
}
