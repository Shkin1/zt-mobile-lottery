package com.iwhalecloud.lottery.redis;

import com.iwhalecloud.lottery.exception.LtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>Title: RedisCacheManager</p>
 * <p>Company:浩鲸云计算科技股份有限公司 </p>
 * <p>Description:
 * 描述：
 * </p>
 * Redis Util
 * 操作字符串：redisTemplate.opsForValue();
 * 操作hash：redisTemplate.opsForHash();
 * 操作list：redisTemplate.opsForList();
 * 操作set：redisTemplate.opsForSet();
 * 操作有序set：redisTemplate.opsForZSet();
 *
 * @author jinpu.shi
 * @version v1.0.0
 * @since 2020-10-26 09:20
 */
@Slf4j
@Component
public class RedisCacheManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }
        catch (Exception e) {
            log.error("redis get exception: {}", e.getMessage(), e);
            return null;
        }
        finally {
            if (redisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        catch (Exception e) {
            log.error("redis set exception: {}", e.getMessage(), e);
            return false;
        }
        finally {
            if (redisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return Long
     */
    public Long incr(String key, long delta) {
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            if (delta < 0) {
                throw new LtException("递增因子必须大于0");
            }
            return redisTemplate.opsForValue().increment(key, delta);
        }
        catch (Exception e) {
            log.error("redis incr error: {}", e.getMessage(), e);
            return null;
        }
        finally {
            if (redisTemplate.getConnectionFactory() != null) {
                RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
            }
        }

    }

    public void pushParamUpdateMsg() {
        redisTemplate.convertAndSend("lottery", "update");
    }

}
