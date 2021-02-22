package com.hcframe.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.hcframe.base.common.ResultVO;
import com.hcframe.es.esmapper.EsMapper;
import com.hcframe.es.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    EsMapper esMapper;

    @Override
    public ResultVO getFullText(HttpServletRequest request, String text, Integer pageNum,
                                Integer pageSize, String indexType) {
        // 获取查询索引数组
        String[] indexArr = indexType.split(",");
        // 返回结果包含字段的数组，默认返回全部，需要返回全部时，必须传null
        String[] includeFields = null;
        // 返回结果排除的字段数组，不排除字段时，必须传null
        String[] excludeFields = null;
        SearchResponse searchResponse = esMapper
                .fullTextSearch(text, pageNum,  pageSize,
                        indexArr, includeFields, excludeFields);
        // 返回结果
        Map<String, Object> resultMap = new HashMap<>(4);
        // 返回结果列表
        List<Map<String, Object>> list = new LinkedList<>();
        // 返回高亮结果列表
        List<Map<String, Object>> highLight = new LinkedList<>();
        // 返回高亮结果
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits) {
            // 获取搜索结果转换成map
            Map<String, Object> dataMap = searchHit.getSourceAsMap();
            // 将map存入返回结果列表
            list.add(dataMap);
            // 获取高亮字段
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            Map<String, Object> highLightMap = new HashMap<>(highlightFields.size());
            for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                HighlightField highlight = highlightFields.get(entry.getKey());
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();
                highLightMap.put(entry.getKey(), fragmentString);
            }
            // 将高亮字段存入高亮List
            highLight.add(highLightMap);
        }
        // 结果列表
        resultMap.put("list", list);
        // 高亮列表
        resultMap.put("highLight", highLight);
        // 返回总数
        resultMap.put("total", searchHits.getTotalHits());
        return ResultVO.getSuccess(resultMap);
    }

    @Override
    public ResultVO expertSearch(HttpServletRequest request, String data, Integer pageNum,
                                 Integer pageSize, String indexType) {
        Map<String ,Object> map = JSON.parseObject(data);
        // 返回结果包含字段的数组，默认返回全部
        String[] includeFields = new String[]{};
        // 返回结果排除的字段数组
        String[] excludeFields = new String[]{};
        // 将查询条件传给es
        SearchResponse searchResponse = esMapper.expertSearch(map,pageNum,pageSize,indexType,includeFields,excludeFields);
        // 得到返回结果中的查询条件
        SearchHits searchHits = searchResponse.getHits();
        // 返回结果的List
        List<Map<String, Object>> list = new LinkedList<>();
        // 遍历返回结果并放入返回结果list中
        for (SearchHit searchHit : searchHits) {
            list.add(searchHit.getSourceAsMap());
        }
        // 返回结果Map
        Map<String, Object> resultMap = new HashMap<>();
        // 将数据放入返回结果Map
        resultMap.put("total", searchHits.getTotalHits());
        resultMap.put("list", list);
        return ResultVO.getSuccess(resultMap);
    }
}
