package com.hcframe.gateway.config;

import com.hcframe.gateway_cas_stater.data.DataStorage;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lhc
 */
public class MyDataStorage implements DataStorage {

    private static final String SESSION = "session";
    private static final String EXPIRE_TIME = "expireTime";

    private Long expireTime;

    @Resource
    private RedisTemplate redisTemplate;

    public MyDataStorage(Long expireTime) {
        if (expireTime==null){
            throw new RuntimeException("expireTime can not be null");
        }
        this.expireTime = expireTime;
    }

    @Override
    public Object getValue(String userKey, String key) {
        Map<String,Object> map = (Map<String, Object>) redisTemplate.opsForHash().get(SESSION, userKey);
        if (map == null) {
            return null;
        }
        if (!map.containsKey(key)) {
            return null;
        }
        return map.get(key);
    }

    @Override
    public void setValue(String userKey, String key, Object attr) {
        Map<String,Object> map = (Map<String, Object>) redisTemplate.opsForHash().get(SESSION, userKey);
        if (map != null) {
            map.put(key, attr);
            redisTemplate.opsForValue().set("session:"+userKey, map, expireTime, TimeUnit.SECONDS);
        } else {
            map = new HashMap<>(2);
            map.put(key, attr);
            map.put(EXPIRE_TIME, expireTime);
            redisTemplate.opsForValue().set("session:"+userKey, map, expireTime, TimeUnit.SECONDS);
        }
    }
}
