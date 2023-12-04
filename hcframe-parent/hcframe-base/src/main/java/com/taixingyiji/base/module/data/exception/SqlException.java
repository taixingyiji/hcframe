package com.taixingyiji.base.module.data.exception;

import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.utils.StringUtils;

public class SqlException {

    public static void operation(int i, String str) {
        if (StringUtils.isEmpty(str)) {
            str = "操作失败";
        }
        if (i < 1) {
            throw new ServiceException(str);
        }
    }

    public static void base(int i, String str) {
        if (StringUtils.isEmpty(str)) {
            str = "操作失败";
        }
        if (i < 1) {
            throw new BaseMapperException(str);
        }
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        if (m == 0) {
            return getAvg(n, nums2);
        }
        if (n == 0) {
            return getAvg(m, nums1);
        }
        return 0;
    }

    public static double getAvg(int n, int[] nums) {
        if (n % 2 == 0) {
            return (nums[n / 2] + nums[n / 2 - 1]) / 2.0;
        } else {
            return (nums[n / 2]);
        }
    }


    public static void main(String[] args) {

    }

}
