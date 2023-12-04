package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.taixingyiji.user.module.userinfo.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
		@RequiresPermissions(value = {"searchLog"})
	    public ResultVO<PageInfo<Map<String,Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLogList(data, webPageInfo);
	    }
	    @GetMapping("/loginlog")
		@RequiresPermissions(value = {"loginLog"})
	    @ApiOperation(value = "登录日志查询模块")
	    public ResultVO<PageInfo<Map<String,Object>>> getLoginLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLoginLogList(data, webPageInfo);
	    }

}
