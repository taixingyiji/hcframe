package com.taixingyiji.es.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.es.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    SearchService searchService;

    @GetMapping("/fulltext")
    public ResultVO getFullText(HttpServletRequest request, String text, Integer pageNum, Integer pageSize, String indexType) {
        return searchService.getFullText(request, text,pageNum,pageSize,indexType);
    }

    @GetMapping("/expertSearch")
    public ResultVO expertSearch(HttpServletRequest request,String data,Integer pageNum,Integer pageSize,String index) {
        return searchService.expertSearch(request, data, pageNum, pageSize, index);
    }

}
