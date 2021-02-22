package com.hcframe.base.common.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {

    public static String setMd5(String  password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public static boolean isEqual(String password, String md5) {
        String str = DigestUtils.md5DigestAsHex(password.getBytes());
        if (md5.equals(str)) {
            return true;
        } else {
            return false;
        }
    }
}
