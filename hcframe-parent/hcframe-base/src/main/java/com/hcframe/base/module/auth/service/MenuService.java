package com.hcframe.base.module.auth.service;


import com.hcframe.base.module.auth.entity.OsSysMenu;
import com.hcframe.base.module.auth.vo.RouterVo;

import java.util.List;

public interface MenuService {

    List<OsSysMenu> getMenuResult();

    List<RouterVo> formatMenu(List<OsSysMenu> menus);

    Object add(OsSysMenu osSysMenu);
}
