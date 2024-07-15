package com.example.MoneyLover.config.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> void setValue(String key, T value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, unit);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return clazz.cast(ops.get(key));
    }

    public <T> void setList(String key, List<T> list, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, list, timeout, unit);
    }

    // Method to get a list of objects
    public <T> List<T> getList(String key, Class<T> clazz) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Object value = ops.get(key);
        if (value instanceof List<?> list) {
            return list.stream().map(item -> clazz.cast(item)).toList();
        }
        return null;
    }


    public void removeValue(String key) {
        redisTemplate.delete(key);
    }
}
