package com.taixingyiji.user.module.auth.mapper;

import com.taixingyiji.base.module.auth.entity.OsSysMenu;

import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className AuthDao
 * @date 2021年04月25日 3:30 下午
 * @description 描述
 */
public interface AuthDao {
    List<Map<String,Object>> selectMenuList(OsSysMenu osSysMenu);
}
