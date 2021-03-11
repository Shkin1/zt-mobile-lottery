package com.iwhalecloud.lottery.config;

import com.iwhalecloud.lottery.pojo.MessageReceive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * <p>Title: RedisChannelConfig</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述:
 * </p>
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-11-22 20:02
 */
@Configuration
public class RedisChannelConfig {
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅主题lottery
        container.addMessageListener(listenerAdapter, new PatternTopic("lottery"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageReceive receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
