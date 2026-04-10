package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.user.module.userinfo.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("log")
public class LogController {

	    final LogService logService;

	    public LogController(LogService logService) {
	        this.logService = logService;
	    }

	    @GetMapping()
		@RequiresPermissions(value = {"searchLog"})
	    public ResultVO<PageInfo<Map<String,Object>>> getLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLogList(data, webPageInfo);
	    }
	    @GetMapping("/loginlog")
		@RequiresPermissions(value = {"loginLog"})
	    public ResultVO<PageInfo<Map<String,Object>>> getLoginLogList(String data, WebPageInfo webPageInfo) {
	        return logService.getLoginLogList(data, webPageInfo);
	    }

}
