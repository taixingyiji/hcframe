package com.hcframe.user.module.auth.service.impl;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.auth.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author wewe
 * @date 2021年4月14日
 * @description 功能级权限增删改查
 */
@Service("userMenuService")
public class MenuServiceImpl implements MenuService {

    private static final OsSysTable OS_SYS_MENU = OsSysTable.builder().tableName("OS_SYS_MENU").tablePk("MENU_ID").build();
    private static final OsSysTable OS_REL_ROLE_MENU = OsSysTable.builder().tableName("OS_REL_ROLE_MENU").tablePk("ROLE_MENU_ID").build();

    @Autowired BaseMapper baseMapper;
    @Autowired TableService tableService;

	@Override
	public ResultVO<Object> addMenu(Map<String, Object> data) {
		if (null == data.get("OS_ID")) {
			return ResultVO.getFailed("操作系统ID不能为空");
		}
		if (StringUtils.isBlank((String) data.get("MENU_NAME"))) {
			return ResultVO.getFailed("菜单名称不能为空");
		}
		Condition condition = Condition.creatCriteria().andEqual("MENU_NAME", data.get("MENU_NAME")).andEqual("DELETED",1).build();
        Long i = baseMapper.count("OS_SYS_MENU", condition);
        if (i > 0L) {
            return ResultVO.getFailed("菜单名称不能重复");
        }
        tableService.saveWithDate(OS_SYS_MENU, data);
		return ResultVO.getSuccess();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultVO<Object> deleteMenu(List<Long> ids) {
		ids.forEach(id -> {
			// 删除菜单及子菜单
			String familyIds = getCascadeList(id);
			tableService.logicDelete(OS_SYS_MENU, familyIds);
			// 删除角色与系统功能关联表OS_REL_ROLE_MENU
			String rmIds = getRmList(familyIds);
			tableService.logicDelete(OS_REL_ROLE_MENU, rmIds);
		});
		return ResultVO.getSuccess();
	}

	private String getRmList(String familyIds) {
		String sql = "select ROLE_MENU_ID from OS_REL_ROLE_MENU where MENU_ID in(" + familyIds + ")";
		List<Map<String,Object>> ids = baseMapper.selectSql(sql);
		StringJoiner joiner = new StringJoiner(",");
        for (Map<String,Object> idm: ids) {
            joiner.add(idm.get("ROLE_MENU_ID").toString());
        }
        return joiner.toString();
	}

	private String getCascadeList(Long id) {
		String sql = "select MENU_ID from OS_SYS_MENU start with MENU_ID=" + id + " connect by prior MENU_ID=PARENT_ID";
		List<Map<String,Object>> ids = baseMapper.selectSql(sql);
		StringJoiner joiner = new StringJoiner(",");
        for (Map<String,Object> idm: ids) {
            joiner.add(idm.get("MENU_ID").toString());
        }
        return joiner.toString();
	}

	@Override
	public ResultVO<Integer> updateMenu(Map<String, Object> data, Integer version) {
		return tableService.updateWithDate(OS_SYS_MENU, data, version);
	}

	@Override
	public ResultVO<PageInfo<Map<String, Object>>> getMenuList(String data, WebPageInfo webPageInfo) {
		return ResultVO.getSuccess(tableService.searchSingleTables(data, OS_SYS_MENU, webPageInfo));
	}

	@Override
	public ResultVO<Object> addRoleMenu(List<Long> roleIds, List<Long> menuIds) {
		roleIds.forEach(roleId -> {
			menuIds.forEach(menuId -> {
				Map<String, Object> data = new HashMap<>();
				data.put("ROLE_ID", roleId);
				data.put("MENU_ID", menuId);
				data.put("VERSION", 1);
				data.put("DELETED", 1);
				tableService.saveWithDate(OS_REL_ROLE_MENU, data);
			});
		});
		return ResultVO.getSuccess();
	}

	@Override
	public ResultVO<Object> deleteRoleMenu(String ids) {
		tableService.logicDelete(OS_REL_ROLE_MENU, ids);
		return ResultVO.getSuccess();
	}

}
