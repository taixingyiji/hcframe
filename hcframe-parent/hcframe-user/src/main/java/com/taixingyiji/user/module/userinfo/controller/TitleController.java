package com.taixingyiji.user.module.userinfo.controller;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.user.module.userinfo.service.TitleService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("title")
public class TitleController {

	  final TitleService titleService;

	    public TitleController(TitleService titleService) {
	        this.titleService = titleService;
	    }

	    @GetMapping("/{titlecode}")
	    public ResultVO<Object> checkExistTitle(@PathVariable String titlecode) {
	        return titleService.checkExistTitle(titlecode);
	    }

	    @PostMapping()
	    @LogAnno(operateType="新增职称信息",moduleName="系统管理-用户管理-职称管理")
		@RequiresPermissions(value = {"system:userManage:title:add"})
	    public ResultVO<Object> addTitle(@RequestParam Map<String,Object> title) {
	        return titleService.addTitle(title);
	    }

	    @PutMapping("/{version}")
	    @LogAnno(operateType="更新职称信息",moduleName="系统管理-用户管理-职称管理")
		@RequiresPermissions(value = {"system:userManage:title:edit"})
	    public ResultVO<Map<String,Object>> updateTitle(@RequestParam Map<String,Object> title,@PathVariable Integer version) {
	        return titleService.updateTitle(title,version);
	    }

	    @DeleteMapping("/{ids}")
	    @LogAnno(operateType="删除职称信息",moduleName="系统管理-用户管理-职称管理")
		@RequiresPermissions(value = {"system:userManage:title:detele"})
	    public ResultVO<Object> deleteTitle(@PathVariable String ids) {
	        return titleService.deleteTitle(ids);
	    }

	    @GetMapping()
		@RequiresPermissions(value = {"titleManage","system:userManage:title:list"},logical = Logical.OR)
	    public ResultVO<PageInfo<Map<String,Object>>> getTitleList(String data, WebPageInfo webPageInfo) {
	        return titleService.getTitleList(data, webPageInfo);
	    }

}
