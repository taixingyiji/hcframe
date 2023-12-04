package com.taixingyiji.base.common.utils;

import java.util.UUID;

/**
 * 生成Token的工具类
 *
 */
public class TokenProccessor {

    private TokenProccessor(){}

    private static final TokenProccessor instance = new TokenProccessor();

    public static TokenProccessor getInstance() {
        return instance;
    }

    /**
     * 生成Token
     * @return
     */
    public String makeToken() {
        String token = System.currentTimeMillis() + UUID.randomUUID().toString();
        EncryptUtil encryptUtil = EncryptUtil.getInstance();
        String str = encryptUtil.MD5(token);
        str = str.replaceAll("/", "");
        str = str.replaceAll("\\+", "");
        str = str.replaceAll("=", "");
        return str;
    }


}
