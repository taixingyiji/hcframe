package com.hcframe.base.common.utils;

import java.util.regex.Pattern;

public class RegexUtil {

    public static boolean isChinese(String str) {
        String pattern = "^([\\u4E00-\\u9FA5])*$";
        return Pattern.matches(pattern, str);
    }
}
