package com.hcframe.user.module.manage.controller;

import com.hcframe.user.module.manage.service.ManageService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhc
 * @date 2021-02-05
 */
@Api(tags = "用户管理")
@RequestMapping("manage")
@RestController
public class ManageController {

    final
    ManageService manageService;

    public ManageController(ManageService manageService) {
        this.manageService = manageService;
    }

}
