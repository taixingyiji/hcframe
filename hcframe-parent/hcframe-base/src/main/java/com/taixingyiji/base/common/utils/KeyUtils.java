package com.taixingyiji.base.common.utils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @className KeyUtils
 * @author lhc
 * @date 2025年12月29日 10:05
 * @description 描述
 * @version 1.0
 */
public class KeyUtils {

    public static final Pattern SAFE_KEY_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]+$");

    public static boolean isUnSafeKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return !key.matches("^[A-Za-z0-9_]+$");
    }


    public static void checkSafeKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key 不能为空");
        }
        if (!SAFE_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException(
                    "非法 key：" + key + "，只允许字母、数字和下划线"
            );
        }
    }

    public static void checkSafeKey(Map<String, ?> map) {
        if (map == null || map.isEmpty()) {
            return; // 空 Map 直接放行（可按你业务改成异常）
        }

        for (String key : map.keySet()) {
            try {
                checkSafeKey(key);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Map 中存在非法 key：" + key,
                        e
                );
            }
        }
    }
}
