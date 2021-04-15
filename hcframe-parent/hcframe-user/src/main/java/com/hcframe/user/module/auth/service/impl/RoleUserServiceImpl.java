package com.hcframe.user.module.auth.service.impl;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.user.module.auth.service.RoleUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author lhc
 * @version 1.0
 * @className RoleUserServiceImpl
 * @date 2021年04月15日 9:09 上午
 * @description 描述
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

    private static final String OS_REL_USER_ROLE = "OS_REL_USER_ROLE";
    private static final String OS_REL_USER_GROUP = "OS_REL_USER_GROUP";

    final BaseMapper baseMapper;

    final TableService tableService;

    public RoleUserServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                               TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    public ResultVO<Object> roleUserBind(Long userId, String roleId) {

        return null;
    }

    @Override
    public ResultVO<Object> roleGroupBind(Long userId, String groupId) {
        return null;
    }
}
