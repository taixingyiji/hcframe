package com.hcframe.user.module.config.service;

import com.hcframe.base.common.ResultVO;
import com.hcframe.user.module.config.entity.UserConfig;

/**
 * @author lhc
 * @version 1.0
 * @className UserConfigService
 * @date 2021年05月26日 9:56 上午
 * @description 描述
 */
public interface UserConfigService {
    ResultVO<Object> saveUserConfig(UserConfig userConfig);

    UserConfig getUserConfig();
}
