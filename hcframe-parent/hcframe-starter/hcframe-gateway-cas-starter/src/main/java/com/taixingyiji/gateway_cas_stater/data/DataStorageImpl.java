package com.taixingyiji.gateway_cas_stater.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lhc
 */
public class DataStorageImpl implements DataStorage {

    private ConcurrentHashMap<String, Map<String, Object>>  storage= new ConcurrentHashMap<>();

    private Long expireTime;

    public DataStorageImpl(Long expireTime) {
        if (expireTime==null){
            throw new RuntimeException("expireTime can not be null");
        }
        this.expireTime = expireTime;
    }

    @Override
    public Object getValue(String userKey, String key) {
        if(storage.get(userKey)==null){
            return null;
        }
        return storage.get(userKey).get(key);
    }

    @Override
    public void setValue(String userKey, String key, Object value) {
        Map<String,Object> userHolder=storage.get(userKey);
        if(userHolder!=null){
            userHolder.put(key,value);
        }else {
            userHolder=new HashMap<>();
            userHolder.put(key,value);
            userHolder.put("expireTime",expireTime);
            storage.put(userKey,userHolder);
        }
    }
}
