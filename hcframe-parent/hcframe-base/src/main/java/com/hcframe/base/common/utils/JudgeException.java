package com.hcframe.base.common.utils;

import com.hcframe.base.common.ServiceException;

public class JudgeException {

    public static void isNull(Object o, String msg) {
        if (o == null) {
            throw new ServiceException(msg);
        }
    }
}
