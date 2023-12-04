package com.taixingyiji.gateway_cas_stater.data;

public interface DataStorage {

    Object getValue(String userKey,String key);

    void setValue(String userKey,String key, Object attr);
}
