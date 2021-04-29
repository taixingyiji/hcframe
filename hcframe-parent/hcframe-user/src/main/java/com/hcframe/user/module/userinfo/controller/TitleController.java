package com.hcframe.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.log.annotation.LogAnno;
import com.hcframe.user.module.userinfo.service.TitleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("title")
@Api(tags = "职称管理")
public class TitleController {

	  final TitleService titleService;

	    public TitleController(TitleService titleService) {
	        this.titleService = titleService;
	    }

	    @GetMapping("/{titlecode}")
	    @ApiOperation(value = "校验职称编码是否存在", notes = "将自动传承key-value对象模式即可")
	    public ResultVO<Object> checkExistTitle(@PathVariable String titlecode) {
	        return titleService.checkExistTitle(titlecode);
	    }

	    @PostMapping()
	    @LogAnno(operateType="新增职称信息",moduleName="系统管理-用户管理-职称管理")
	    @ApiOperation(value = "新增title", notes = "将自动传承key-value对象模式即可")
		@RequiresPermissions(value = {"system:userManage:title:add"})
	    public ResultVO<Object> addTitle(@RequestParam Map<String,Object> title) {
	        return titleService.addTitle(title);
	    }

	    @PutMapping("/{version}")
	    @LogAnno(operateType="更新职称信息",moduleName="系统管理-用户管理-职称管理")
	    @ApiOperation(value = "更新title")
		@RequiresPermissions(value = {"system:userManage:title:edit"})
	    public ResultVO<Integer> updateTitle(@RequestParam Map<String,Object> title,@PathVariable Integer version) {
	        return titleService.updateTitle(title,version);
	    }

	    @DeleteMapping("/{ids}")
	    @LogAnno(operateType="删除职称信息",moduleName="系统管理-用户管理-职称管理")
	    @ApiOperation(value = "删除title")
		@RequiresPermissions(value = {"system:userManage:title:detele"})
	    public ResultVO<Object> deleteTitle(@PathVariable String ids) {
	        return titleService.deleteTitle(ids);
	    }

	    @GetMapping()
	    @ApiOperation(value = "获取职称列表")
		@RequiresPermissions(value = {"titleManage","system:userManage:title:list"},logical = Logical.OR)
	    public ResultVO<PageInfo<Map<String,Object>>> getTitleList(String data, WebPageInfo webPageInfo) {
	        return titleService.getTitleList(data, webPageInfo);
	    }

}
