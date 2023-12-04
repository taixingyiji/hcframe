package com.taixingyiji.base.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import com.taixingyiji.base.module.auth.service.AuthFunctionService;
import com.taixingyiji.base.module.auth.service.MenuService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhc
 * @date 2020-12-09
 * @decription 权限接口
 */
@RestController
@Api(tags = "权限管理")
@RequestMapping("auth/function")
public class AuthFunctionController {

    @Autowired
    AuthFunctionService authService;

    @Autowired
    MenuService menuService;


    @GetMapping("menu")
    public ResultVO getMenuResult() {
        List<OsSysMenu> menus = menuService.getMenuResult();
        return ResultVO.getSuccess(menuService.formatMenu(menus));
    }

    @PostMapping("menu")
    public ResultVO addAuthResult(OsSysMenu osSysMenu) {
        return ResultVO.getSuccess(menuService.add(osSysMenu));
    }

}
