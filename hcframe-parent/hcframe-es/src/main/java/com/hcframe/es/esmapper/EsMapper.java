package com.hcframe.es.esmapper;

import org.elasticsearch.action.search.SearchResponse;

import java.util.Map;

public interface EsMapper {


    SearchResponse fullTextSearch(String text, Integer pageNum,  Integer pageSize, String[] indexArr, String[] includeFields, String[] excludeFields);


    SearchResponse expertSearch(Map<String, Object> map, Integer pageNum, Integer pageSize, String indexArr, String[] includeFields, String[] excludeFields);
}
