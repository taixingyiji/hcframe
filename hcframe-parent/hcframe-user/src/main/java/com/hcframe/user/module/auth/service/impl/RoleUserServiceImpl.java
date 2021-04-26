package com.hcframe.user.module.auth.service.impl;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.auth.service.RoleUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String USER_GROUP_ID = "USER_GROUP_ID";
    private static final String USER_ROLE_ID = "USER_ROLE_ID";
    private static final String OS_REL_USER_GROUP = "OS_REL_USER_GROUP";
    private static final String COMMA = ",";

    final BaseMapper baseMapper;

    final TableService tableService;

    public RoleUserServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                               TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleUserBind(String userId, String roleIds) {
        JudgeException.isNull(userId,"userId 不能为空");
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_USER_ROLE).tablePk(USER_ROLE_ID).build();
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).build();
        baseMapper.deleteByCondition(OS_REL_USER_ROLE, condition);
        if (!StringUtils.isEmpty(roleIds)) {
            for (String roleId : roleIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("USER_ID", userId.replaceAll("\"",""));
                map.put("ROLE_ID", Integer.parseInt(roleId));
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> roleGroupBind(String userId, String groupIds) {
        JudgeException.isNull(userId,"userId 不能为空");
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).build();
        baseMapper.deleteByCondition(OS_REL_USER_GROUP, condition);
        OsSysTable osSysTable = OsSysTable.builder().tableName(OS_REL_USER_GROUP).tablePk(USER_GROUP_ID).build();
        if (!StringUtils.isEmpty(groupIds)) {
            for (String groupId : groupIds.split(COMMA)) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("USER_ID", userId.replaceAll("\"",""));
                map.put("GROUP_ID", groupId);
                tableService.save(osSysTable, map);
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<Object> getUserRole(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).andEqual("DELETED",1).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(OS_REL_USER_ROLE, condition);
        return ResultVO.getSuccess(list);
    }

    @Override
    public ResultVO<Object> getUserGroup(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("USER_ID",userId).andEqual("DELETED",1).build();
        List<Map<String,Object>> list = baseMapper.selectByCondition(OS_REL_USER_GROUP, condition);
        return ResultVO.getSuccess(list);
    }

}
