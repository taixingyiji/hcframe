package com.taixingyiji.base.common.utils;

import com.taixingyiji.base.common.ServiceException;
import org.springframework.util.StringUtils;

public class EmptyCheckUtils {

    public static boolean checkWithException(Object object,String info){
        if (StringUtils.isEmpty(object)) {
            throw new ServiceException(info);
        } else {
            return true;
        }
    }
}
