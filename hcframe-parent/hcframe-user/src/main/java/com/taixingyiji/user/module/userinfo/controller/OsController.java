package com.taixingyiji.user.module.userinfo.controller;

import java.util.Map;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.user.module.userinfo.service.OsService;


@RestController
@RequestMapping("os")
public class OsController {

    final OsService osService;

    public OsController(OsService osService) {
        this.osService = osService;
    }

    @GetMapping("/{oscode}")
    public ResultVO<Object> checkExistOs(@PathVariable String oscode) {
        return osService.checkExistOs(oscode);
    }

    @GetMapping("validUrl")
    public ResultVO<Object> validUrl(String url) {
        return osService.validUrl(url);
    }

    @PostMapping()
    @LogAnno(operateType = "新增系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    public ResultVO<Object> addOs(@RequestParam Map<String, Object> os) {
        return osService.addOs(os);
    }

    @PutMapping("/{version}")
    @LogAnno(operateType = "更新系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    public ResultVO<Map<String,Object>> updateOs(@RequestParam Map<String, Object> os, @PathVariable Integer version) {
        return osService.updateOs(os, version);
    }

    @DeleteMapping("/{ids}")
    @LogAnno(operateType = "删除系统信息", moduleName = "系统管理-权限管理-系统信息管理")
    public ResultVO<Object> deleteOs(@PathVariable String ids) {
        return osService.deleteOs(ids);
    }

    @GetMapping()
    public ResultVO<PageInfo<Map<String, Object>>> getOsList(String data, WebPageInfo webPageInfo) {
        return osService.getOsList(data, webPageInfo);
    }

}
