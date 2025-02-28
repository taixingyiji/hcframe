package com.taixingyiji.base.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.WebPageInfo;
import com.github.pagehelper.PageHelper;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.cache.CacheService;
import com.taixingyiji.base.module.cache.base.BaseCache;
import com.taixingyiji.base.module.cache.emum.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class MyPageHelper {

    private static MyPageHelper myPageHelper;
    @Autowired
    BaseCache baseCache;

    @Autowired
    FrameConfig frameConfig;

    @PostConstruct
    public void init() {
        myPageHelper = this;
    }

    public static void start(WebPageInfo webPageInfo) {
        if (WebPageInfo.hasSort(webPageInfo)) {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql()).setAsyncCount(true);
        } else {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize()).setAsyncCount(true);
        }
    }

    public static void noCount(WebPageInfo webPageInfo) {
        if (WebPageInfo.hasSort(webPageInfo)) {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), false).setOrderBy(webPageInfo.getSortSql());
        } else {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), false);
        }
    }

    public static PageInfo<Map<String, Object>> noCount(WebPageInfo webPageInfo, Supplier<List<Map<String, Object>>> querySupplier) {
        if (WebPageInfo.hasSort(webPageInfo)) {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql()).count(false).doSelectPageInfo(querySupplier::get);
        } else {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize()).count(false).doSelectPageInfo(querySupplier::get);
        }
    }

    public static PageInfo<Map<String, Object>> myStart(WebPageInfo webPageInfo, Supplier<List<Map<String, Object>>> querySupplier) {
        if (WebPageInfo.hasSort(webPageInfo)) {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql()).doSelectPageInfo(querySupplier::get);
        } else {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize()).doSelectPageInfo(querySupplier::get);
        }
    }

    public static PageInfo<Map<String, Object>> start(WebPageInfo webPageInfo, String sql, Supplier<List<Map<String, Object>>> querySupplier) {
        Long currentTime = System.currentTimeMillis();
        String data = myPageHelper.baseCache.get(CacheType.pageCache.toString(), sql, String.class);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(data)) {
            PageInfo<Map<String, Object>> result = myStart(webPageInfo, querySupplier);
            jsonObject.set("time", currentTime);
            jsonObject.set("count", result.getTotal());
            if(result.getTotal() > myPageHelper.frameConfig.getPageMaxCache()){
                myPageHelper.baseCache.add(CacheType.pageCache.toString(), sql, jsonObject.toString(), String.class);
            }
            return result;
        } else {
            JSONObject cacheJson = JSONUtil.parseObj(data);
            Long saveTime = (Long) cacheJson.get("time");
            long timeDiffInSeconds = (currentTime - saveTime) / 1000;
            if (timeDiffInSeconds > myPageHelper.frameConfig.getPageCacheTime()) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PageInfo<Map<String, Object>> result = myStart(webPageInfo, querySupplier);
                        jsonObject.set("time", currentTime);
                        jsonObject.set("count", result.getTotal());
                        myPageHelper.baseCache.add(CacheType.pageCache.toString(), sql, jsonObject.toString(), String.class);
                    }
                });
                thread.start();
            }
            long total = cacheJson.get("count", Long.class);
            noCount(webPageInfo);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(querySupplier.get());
            pageInfo.setTotal(total);
            return pageInfo;
        }
    }

    public static void orderBy(String sortField, String order) {
        PageHelper.orderBy(sortField + " " + order);
    }
}
