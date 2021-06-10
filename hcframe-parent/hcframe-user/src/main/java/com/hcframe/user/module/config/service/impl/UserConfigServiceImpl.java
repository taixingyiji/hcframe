package com.hcframe.user.module.config.service.impl;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.common.utils.ObjectUtil;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.redis.RedisUtil;
import com.hcframe.user.common.config.UserConstant;
import com.hcframe.user.module.config.entity.UserConfig;
import com.hcframe.user.module.config.service.UserConfigService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className UserConfigServiceImpl
 * @date 2021年05月26日 9:56 上午
 * @description 描述
 */
@Service
public class UserConfigServiceImpl implements UserConfigService {

    final BaseMapper baseMapper;

    final RedisUtil redisUtil;

    public UserConfigServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                                 RedisUtil redisUtil) {
        this.baseMapper = baseMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public ResultVO<Object> saveUserConfig(UserConfig userConfig) {
        redisUtil.hdel(UserConstant.USER,UserConstant.USER_CONFIG);
        JudgeException.isNull(userConfig.getUserTableName(), "userTableAlias不能为null");
        if (StringUtils.isEmpty(userConfig.getUserConfigId())) {
            baseMapper.save(userConfig);
        } else {
            baseMapper.updateByPk(userConfig);
        }
        return ResultVO.getSuccess();
    }

    @Override
    public UserConfig getUserConfig() {
        UserConfig userConfig = (UserConfig) redisUtil.hget("user","userConfig");
        if (userConfig == null) {
            List<Map<String, Object>> list = baseMapper.selectAll("OS_SYS_USER_CONFIG");
            userConfig = ObjectUtil.mapToObj(list.get(0), UserConfig.class);
            redisUtil.hset("user","userConfig", userConfig);
        }
        return userConfig;
    }
}
