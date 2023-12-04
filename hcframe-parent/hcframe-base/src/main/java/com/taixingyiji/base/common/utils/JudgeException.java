package com.taixingyiji.base.common.utils;

import com.taixingyiji.base.common.ServiceException;

import java.util.Objects;

public class JudgeException {

    private static final String NULL = "null";

    public static void isNull(Object o, String msg) {
        if (o == null||Objects.equals(o, NULL)) {
            throw new ServiceException(msg);
        }
    }
}
