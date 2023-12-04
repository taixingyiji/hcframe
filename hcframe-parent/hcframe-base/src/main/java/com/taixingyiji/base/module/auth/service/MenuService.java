package com.taixingyiji.base.module.auth.service;


import com.taixingyiji.base.module.auth.entity.OsSysMenu;
import com.taixingyiji.base.module.auth.vo.RouterVo;

import java.util.List;

public interface MenuService {

    List<OsSysMenu> getMenuResult();

    List<RouterVo> formatMenu(List<OsSysMenu> menus);

    Object add(OsSysMenu osSysMenu);
}
