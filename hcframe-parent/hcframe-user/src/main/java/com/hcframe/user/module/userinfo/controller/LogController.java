package com.hcframe.user.module.userinfo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.user.module.userinfo.service.LogService;
import com.hcframe.user.module.userinfo.service.OrgService;
import com.hcframe.user.module.userinfo.service.OsService;
import com.hcframe.user.module.userinfo.service.TitleService;

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
