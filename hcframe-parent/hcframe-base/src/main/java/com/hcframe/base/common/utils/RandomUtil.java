package com.hcframe.base.common.utils;

import java.util.Random;

public class RandomUtil {


    public static String getrandom() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            code.append(r);
        }
        return code.toString();

    }
}
