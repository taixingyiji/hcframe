package com.taixingyiji.base.module.auth.dao;

import com.taixingyiji.base.common.Mapper;
import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表(OsSysMenu)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-09 09:57:20
 */
public interface OsSysMenuDao extends Mapper<OsSysMenu> {

    List<OsSysMenu> selectMenu();

    List<OsSysMenu> selectMenuByUser(@Param("paths")String paths);

    Set<String> selectAllAuth();

    List<OsSysMenu> selectMenuList(@Param("menu")OsSysMenu osSysMenu);
}
