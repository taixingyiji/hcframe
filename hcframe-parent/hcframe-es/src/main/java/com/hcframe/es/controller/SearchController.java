package com.hcframe.es.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.es.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "ES搜索相关接口")
@RequestMapping("/search")
public class SearchController {

    @Resource
    SearchService searchService;

    @ApiOperation(value = "全文检索接口样例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "text", value = "关键词", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true),
            @ApiImplicitParam(name = "indexType", value = "索引，以逗号隔开", required = true),
    })
    @GetMapping("/fulltext")
    public ResultVO getFullText(HttpServletRequest request, String text, Integer pageNum, Integer pageSize, String indexType) {
        return searchService.getFullText(request, text,pageNum,pageSize,indexType);
    }

    @ApiOperation(value = "高级检索接口样例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "查询条件，在前端将查询对象通过JSON.toString(obj)方法进行格式化成字符串", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", required = true),
            @ApiImplicitParam(name = "index", value = "索引", required = true),
    })
    @GetMapping("/expertSearch")
    public ResultVO expertSearch(HttpServletRequest request,String data,Integer pageNum,Integer pageSize,String index) {
        return searchService.expertSearch(request, data, pageNum, pageSize, index);
    }

}
