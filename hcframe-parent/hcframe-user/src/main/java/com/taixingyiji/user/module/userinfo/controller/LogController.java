package com.taixingyiji.user.module.userinfo.controller;

import java.util.Map;

import com.taixingyiji.user.module.userinfo.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("log")
@Api(tags = "日志查询")
public class LogController {
	
	    final LogService logService;

	    public LogController(LogService logService) {
	        this.logService = logService;
	    }
	    
	    @GetMapping()
	    @ApiOperation(value = "日志查询模块")
	    public ResultVO<PageInfo<Map<String,Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLogList(data, webPageInfo);
	    }

}
