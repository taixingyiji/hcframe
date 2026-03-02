package com.taixingyiji.base.module.auth.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.module.auth.service.FtUserService;
import com.taixingyiji.base.module.log.annotation.LogAnno;
import com.taixingyiji.base.module.shiro.service.ShiroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * (FtUser)表控制层
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
@RestController
@Tag(name = "用户相关接口")
@RequestMapping("ftUser")
public class FtUserController {
    /**
     * 服务对象
     */
    @Autowired
    private FtUserService ftUserService;

    @Autowired
    ShiroService shiroService;

    @Operation(summary = "用户登陆")
    //@LogAnno(operateType = "用户登录")
    @PostMapping("login")
    public ResultVO login(HttpServletRequest request, 
                         @Parameter(description = "用户名", required = true) String username, 
                         @Parameter(description = "密码", required = true) String password) {
        return ftUserService.login(request, username, password);
    }

    @LogAnno(operateType = "用户登出", isBefore = true)
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public ResultVO logOut(HttpServletRequest request) {
        String token = request.getHeader("X-Access-Token");
        shiroService.logout(token);
        return ResultVO.getSuccess();
    }


}
