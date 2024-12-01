package org.aurora.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    public RedisUtils(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置 key 的值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 key 的值和过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 设置 key 的值和过期时间
     */
    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 设置多个 key 的值
     */
    public void set(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 获取 key 的值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取多个 key 的值
     */
    public List<Object> get(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 设置 key 的值
     */
    public void setStr(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置 key 的值和过期时间
     */
    public void setStr(String key, String value, long expire, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    /**
     * 设置 key 的值和过期时间
     */
    public void setStr(String key, String value, Duration timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 设置多个 key 的值
     */
    public void setStr(Map<String, String> map) {
        stringRedisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 获取 key 的值
     */
    public String getStr(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取多个 key 的值
     */
    public List<String> getStr(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 删除 key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除多个 key
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置 key 的过期时间
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取所有匹配给定模式的 key
     * 例: pattern=* 获取所有 key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 获取 key 的过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 将 key 的整数值递增 delta
     */
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将 key 的整数值递减 delta
     */
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 将值追加到列表 key
     */
    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 获取列表 key 中从开始到结束的元素
     * 例: start=-10 end=-1 获取最后 10 个元素
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 裁剪列表 key 保留从开始到结束的元素
     */
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }
}
