package com.hcframe.user.module.auth.mapper.impl;

import com.hcframe.base.module.auth.entity.OsSysMenu;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.user.module.auth.mapper.AuthDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className AuthDaoImpl
 * @date 2021年04月25日 3:30 下午
 * @description 描述
 */
@Component
public class AuthDaoImpl implements AuthDao {

    final BaseMapper baseMapper;

    public AuthDaoImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public List<Map<String,Object>> selectMenuList(OsSysMenu osSysMenu) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT OS_SYS_MENU.MENU_ID,\n" +
                "               OS_SYS_MENU.MENU_NAME,\n" +
                "               OS_SYS_MENU.PARENT_ID,\n" +
                "               OS_SYS_MENU.PATH,\n" +
                "               OS_SYS_MENU.COMPONENT,\n" +
                "               OS_SYS_MENU.IS_CACHE,\n" +
                "               OS_SYS_MENU.VISIBLE,\n" +
                "               OS_SYS_MENU.IS_FRAME,\n" +
                "               OS_SYS_MENU.MENU_TYPE,\n" +
                "               OS_SYS_MENU.ORDER_NUM,\n" +
                "               OS_SYS_MENU.MENU_STATUS,\n" +
                "               OS_SYS_MENU.PERMS,\n" +
                "               OS_SYS_MENU.AFFIX,\n" +
                "               OS_SYS_MENU.BREADCRUMB,\n" +
                "               OS_SYS_MENU.AlWAYSSHOW,\n" +
                "               OS_SYS_MENU.REMARK,\n" +
                "               OS_SYS_MENU.UPDATE_TIME,\n" +
                "               OS_SYS_MENU.CREATE_TIME,\n" +
                "               OS_SYS_MENU.ICON,\n" +
                "               OS_SYS_MENU.OS_ID,\n" +
                "               OS_SYS_MENU.VERSION,\n" +
                "               OS_SYS_MENU.DELETED\n" +
                "        FROM OS_SYS_MENU\n" +
                "        WHERE  DELETED = 1");
        if (!StringUtils.isEmpty(osSysMenu.getMenuName())) {
            stringBuilder.append(" AND MENU_NAME like '%").append(osSysMenu.getMenuName()).append("%' ");
        }
        if (!StringUtils.isEmpty(osSysMenu.getOsId())) {
            stringBuilder.append(" AND  OS_ID = ").append(osSysMenu.getOsId()).append(" ");
        }
        if (!StringUtils.isEmpty(osSysMenu.getStatus())) {
            stringBuilder.append("AND  MENU_STATUS = '").append(osSysMenu.getStatus()).append("'");
        }
        stringBuilder.append("ORDER BY OS_SYS_MENU.PARENT_ID ASC,\n" +
                "                 OS_SYS_MENU.ORDER_NUM ASC");
        return baseMapper.selectSql(stringBuilder.toString());
    }
}
