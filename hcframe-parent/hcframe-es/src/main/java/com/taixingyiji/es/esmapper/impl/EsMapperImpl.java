package com.taixingyiji.es.esmapper.impl;

import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.es.esmapper.EsMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EsMapperImpl implements EsMapper {

    @Autowired
    private  RestHighLevelClient client;


    @Override
    public SearchResponse fullTextSearch(String text, Integer pageNum,
                                         Integer pageSize, String[] indexArr, String[] includeFields,
                                         String[] excludeFields) {
        // 设置高亮配置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlight = new HighlightBuilder.Field("*");
        highlight.highlighterType("unified");
        highlightBuilder.field(highlight);
        // 配置搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .highlighter(highlightBuilder)
                .query(QueryBuilders.queryStringQuery(text))
                .size(pageSize)
                .from((pageNum - 1) * pageSize)
                .fetchSource(includeFields,excludeFields);
        // 建立搜索Request
        SearchRequest searchRequest = new SearchRequest(indexArr);
        searchRequest.source(searchSourceBuilder);
        try {
            // 通过客户端搜索
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("全文检索，全文检索服务器异常");
        }
    }

    @Override
    public SearchResponse  expertSearch(Map<String, Object> map, Integer pageNum, Integer pageSize, String indexArr, String[] includeFields, String[] excludeFields)  {
        // 建立联合查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 遍历查询条件map，进行模糊查询
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            key = key + ".keyword";
            boolQueryBuilder.filter(QueryBuilders.wildcardQuery(key, "*" + entry.getValue() + "*"));
        }
        // 创建搜索builder对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .query(boolQueryBuilder)
                .size(pageSize)
                .from((pageNum - 1) * pageSize)
                .fetchSource(includeFields,excludeFields);
        SearchRequest searchRequest = new SearchRequest(indexArr);
        searchRequest.source(searchSourceBuilder);
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ServiceException("高级检索，全文检索服务器异常");
        }
    }
}
