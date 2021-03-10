package com.hcframe.config.module.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.auth.entity.FtUser;
import com.hcframe.base.module.data.module.BaseMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @Autowired
    BaseMapper baseMapper;

    @GetMapping("/test")
    public ResultVO<String> getUser(HttpServletRequest request) {
        return ResultVO.getSuccess("token");
    }
}
