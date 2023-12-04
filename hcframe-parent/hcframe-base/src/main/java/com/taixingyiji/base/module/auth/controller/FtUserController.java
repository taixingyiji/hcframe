package com.taixingyiji.base.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.auth.service.FtUserService;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * (FtUser)表控制层
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
@RestController
@Api(tags = "用户相关接口")
@RequestMapping("ftUser")
public class FtUserController {
    /**
     * 服务对象
     */
    @Autowired
    private FtUserService ftUserService;

    @Autowired
    ShiroService shiroService;

    @ApiOperation(value = "用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    //@LogAnno(operateType = "用户登录")
    @PostMapping("login")
    public ResultVO login(HttpServletRequest request, String username, String password) {
        return ftUserService.login(request, username, password);
    }

    @LogAnno(operateType = "用户登出", isBefore = true)
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    public ResultVO logOut(HttpServletRequest request) {
        String token = request.getHeader("X-Access-Token");
        shiroService.logout(token);
        return ResultVO.getSuccess();
    }


}
