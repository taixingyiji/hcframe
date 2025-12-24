package com.taixingyiji.base.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.SortItem;
import com.taixingyiji.base.common.WebPageInfo;
import com.github.pagehelper.PageHelper;
import com.taixingyiji.base.common.config.FrameConfig;
import com.taixingyiji.base.module.cache.base.BaseCache;
import com.taixingyiji.base.module.cache.emum.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Supplier;

import static com.taixingyiji.base.common.WebPageInfo.ASC;
import static com.taixingyiji.base.common.WebPageInfo.DESC;

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

    /**
     * 校验单个字段和排序方向是否合法
     */
    private static boolean isSafeField(String field, String order, Set<String> allowedFields) {
        if (StringUtils.isBlank(field)) return false;
        // 字段白名单校验，如果不传 allowedFields，可传 null，则只做格式校验
        if (allowedFields != null && !allowedFields.contains(field)) return false;
        // 字段名格式安全
        if (!field.matches("^[a-zA-Z0-9_]+$")) return false;
        // 排序方向安全
        if (StringUtils.isBlank(order)) return true; // 默认 asc
        return order.equalsIgnoreCase(ASC) || order.equalsIgnoreCase(DESC);
    }

    /**
     * 将 List<SortItem> 转换为 PageHelper 可用 ORDER BY 字符串
     *
     * @param sortList List<SortItem> 前端传入的排序字段
     * @param allowedFields 可选业务白名单字段（可为 null）
     * @return 安全的 ORDER BY 字符串，如果没有合法字段返回 null
     */
    public static String buildOrderBy(List<SortItem> sortList, Set<String> allowedFields) {
        if (sortList == null || sortList.isEmpty()) return null;

        List<String> orderParts = new ArrayList<>();
        for (SortItem item : sortList) {
            if (item == null) continue;
            String field = item.getField();
            String order = item.getOrder();

            if (!isSafeField(field, order, allowedFields)) continue;

            String dir = DESC.equalsIgnoreCase(order) ? DESC : ASC;
            orderParts.add(field + " " + dir);
        }

        return orderParts.isEmpty() ? null : String.join(", ", orderParts);
    }

    public static void start(WebPageInfo webPageInfo) {
        if (WebPageInfo.hasSortList(webPageInfo)) {
            List<SortItem> sortList = JSONUtil.toList(JSONUtil.parseArray(webPageInfo.getSortList()), SortItem.class);
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), buildOrderBy(sortList, new HashSet<>())).setAsyncCount(true);
        } else if (WebPageInfo.hasSort(webPageInfo)) {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql()).setAsyncCount(true);
        } else {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize()).setAsyncCount(true);
        }
    }

    public static void noCount(WebPageInfo webPageInfo) {
        if (WebPageInfo.hasSortList(webPageInfo)) {
            List<SortItem> sortList = JSONUtil.toList(JSONUtil.parseArray(webPageInfo.getSortList()), SortItem.class);
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), false).setOrderBy(buildOrderBy(sortList, new HashSet<>()));
        } else if (WebPageInfo.hasSort(webPageInfo)) {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), false).setOrderBy(webPageInfo.getSortSql());
        } else {
            PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), false);
        }
    }

    public static PageInfo<Map<String, Object>> noCount(WebPageInfo webPageInfo, Supplier<List<Map<String, Object>>> querySupplier) {
        if (WebPageInfo.hasSortList(webPageInfo)) {
            List<SortItem> sortList = JSONUtil.toList(JSONUtil.parseArray(webPageInfo.getSortList()), SortItem.class);
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), buildOrderBy(sortList, new HashSet<>())).count(false).doSelectPageInfo(querySupplier::get);
        } else if (WebPageInfo.hasSort(webPageInfo)) {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), webPageInfo.getSortSql()).count(false).doSelectPageInfo(querySupplier::get);
        } else {
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize()).count(false).doSelectPageInfo(querySupplier::get);
        }
    }

    public static PageInfo<Map<String, Object>> myStart(WebPageInfo webPageInfo, Supplier<List<Map<String, Object>>> querySupplier) {
        if (WebPageInfo.hasSortList(webPageInfo)) {
            List<SortItem> sortList = JSONUtil.toList(JSONUtil.parseArray(webPageInfo.getSortList()), SortItem.class);
            return PageHelper.startPage(webPageInfo.getPageNum(), webPageInfo.getPageSize(), buildOrderBy(sortList, new HashSet<>())).doSelectPageInfo(querySupplier::get);
        } else if (WebPageInfo.hasSort(webPageInfo)) {
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
            if (result.getTotal() > myPageHelper.frameConfig.getPageMaxCache()) {
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
        if (!WebPageInfo.isSafeOrderBy(order)) {
            throw new ServiceException("order 不合法");
        }
        PageHelper.orderBy(sortField + " " + order);
    }
}
