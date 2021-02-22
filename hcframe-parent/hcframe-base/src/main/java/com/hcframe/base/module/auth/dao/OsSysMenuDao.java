package com.hcframe.base.module.auth.dao;

import com.hcframe.base.common.Mapper;
import com.hcframe.base.module.auth.entity.OsSysMenu;

import java.util.List;

/**
 * 菜单权限表(OsSysMenu)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-09 09:57:20
 */
public interface OsSysMenuDao extends Mapper<OsSysMenu> {

    List<OsSysMenu> selectMenu();
}
