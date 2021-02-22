package com.hcframe.base.common.utils;

import com.hcframe.base.common.WebPageInfo;
import com.github.pagehelper.PageHelper;

public class MyPageHelper {

    public static void start(WebPageInfo webPageInfo) {
        if (WebPageInfo.hasSort(webPageInfo)) {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql());
        } else {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize());
        }
    }
}
