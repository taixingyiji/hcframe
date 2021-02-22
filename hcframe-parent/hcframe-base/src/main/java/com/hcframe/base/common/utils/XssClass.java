package com.hcframe.base.common.utils;

import static org.thymeleaf.util.StringUtils.split;

public class XssClass {

    public static boolean sqlInj(String str){
        String injStr = "'|and|exec|insert|select|delete|update|"+
        "count|*|%|chr|mid|master|truncate|char|declare|;|or|+|,|<script>";
        String[] injStra = split(injStr,"|");
        for (String s : injStra) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
