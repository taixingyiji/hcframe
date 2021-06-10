package com.hcframe.user.module.config.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.user.module.config.entity.UserConfig;
import com.hcframe.user.module.config.service.UserConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhc
 * @version 1.0
 * @className UserConfigController
 * @date 2021年05月26日 9:55 上午
 * @description 描述
 */
@RestController
@RequestMapping("config")
public class UserConfigController {

    final UserConfigService userConfigService;

    public UserConfigController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @PostMapping("saveConfig")
    public ResultVO<Object> saveUserConfig(UserConfig userConfig) {
        return userConfigService.saveUserConfig(userConfig);
    }

    @GetMapping("")
    public ResultVO<Object> getUserConfig() {
        return ResultVO.getSuccess(userConfigService.getUserConfig());
    }

}
