package com.taixingyiji.user.module.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.taixingyiji.user.module.auth.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;

/**
 * @author wewe
 * @date 2021年4月14日
 * @description 功能级权限增删改查
 */

@Service("userMenuService")
public class MenuServiceImpl2 implements MenuService {

	private static final OsSysTable OS_SYS_MENU = OsSysTable.builder().tableName("OS_SYS_MENU").tablePk("MENU_ID").build();
	private static final OsSysTable OS_REL_ROLE_MENU = OsSysTable.builder().tableName("OS_REL_ROLE_MENU").tablePk("ROLE_MENU_ID").build();
	private static final OsSysTable OS_REL_ROLE_OS = OsSysTable.builder().tableName("OS_REL_ROLE_OS").tablePk("ROLE_OS_ID").build();

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
		if (StringUtils.isBlank((String) data.get("PATH"))) {
			return ResultVO.getFailed("PATH不能为空");
		}
		Condition condition = Condition.creatCriteria().andEqual("MENU_NAME", data.get("MENU_NAME")).andEqual("DELETED",1).build();
		if (baseMapper.count("OS_SYS_MENU", condition) > 0L) {
			return ResultVO.getFailed("菜单名称不能重复");
		}
		Condition condition1 = Condition.creatCriteria().andEqual("PATH", data.get("PATH")).andEqual("DELETED",1).build();
		if (baseMapper.count("OS_SYS_MENU", condition1) > 0L) {
			return ResultVO.getFailed("PATH不能重复");
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
			if (StringUtils.isNotBlank(rmIds)) {
				tableService.logicDelete(OS_REL_ROLE_MENU, rmIds);
			}
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
	@Transactional
	public ResultVO<Object> addRoleMenu(Long roleId, List<String> menuIds) {
		baseMapper.deleteByCondition(OS_REL_ROLE_OS.getTableName(), Condition.creatCriteria().andEqual("ROLE_ID", roleId).build());
		baseMapper.deleteByCondition(OS_REL_ROLE_MENU.getTableName(), Condition.creatCriteria().andEqual("ROLE_ID", roleId).build());
		menuIds.forEach(menuId -> {
			if (menuId.startsWith("OS")) {
				Map<String, Object> data = new HashMap<>();
				data.put("ROLE_ID", roleId);
				data.put("OS_ID", Long.getLong(menuId.substring(2)));
				data.put("VERSION", 1);
				data.put("DELETED", 1);
				tableService.saveWithDate(OS_REL_ROLE_OS, data);
			} else {
				Map<String, Object> data = new HashMap<>();
				data.put("ROLE_ID", roleId);
				data.put("MENU_ID", menuId);
				data.put("VERSION", 1);
				data.put("DELETED", 1);
				tableService.saveWithDate(OS_REL_ROLE_MENU, data);
			}
		});
		return ResultVO.getSuccess();
	}

	@Override
	public ResultVO<Object> getMenuTree() {
		List<Map<String, Object>> osList = baseMapper.selectSql(
				"SELECT OS_ID AS \"id\",OS_NAME AS \"label\" FROM OS_SYS_OS WHERE DELETED=1");
		osList.forEach(os -> {
			List<Map<String, Object>> menuList = baseMapper.selectSql(
					"SELECT MENU_ID AS \"id\",MENU_NAME AS \"label\",PARENT_ID AS \"pid\" FROM OS_SYS_MENU WHERE DELETED=1 AND OS_ID=" + os.get("id"));
			os.put("children", buildTree(menuList));
			os.put("id", "OS" + os.get("id"));
		});
		return ResultVO.getSuccess(osList);
	}

	private Object buildTree(List<Map<String, Object>> menuList) {
		List<Map<String, Object>> tree = new ArrayList<>();
		for(Map<String, Object> node : menuList) {
			if((Long)node.get("pid") == 0L){
				tree.add(findChild(node,menuList));
			}
		}
		return tree;
	}

	private Map<String, Object> findChild(Map<String, Object> node, List<Map<String, Object>> menuList) {
		for(Map<String, Object> n:menuList){
			if(n.get("pid") == node.get("id")){
				if(node.get("children") == null){
					node.put("children", new ArrayList<Map<String, Object>>());
				}
				((List<Map<String,Object>>) node.get("children")).add(findChild(n,menuList));
			}
		}
		return node;
	}

	@Override
	public ResultVO<Object> getSelectedMenu(Long roleId) {
		List<Object> ids = new ArrayList<>();
		List<Map<String, Object>> osList = baseMapper.selectSql("SELECT OS_ID FROM OS_REL_ROLE_OS WHERE DELETED=1 AND ROLE_ID=" + roleId);
		osList.forEach(os -> {
			ids.add("OS" + os.get("OS_ID"));
		});
		List<Map<String, Object>> menuList = baseMapper.selectSql("SELECT MENU_ID FROM OS_REL_ROLE_MENU WHERE DELETED=1 AND ROLE_ID=" + roleId);
		menuList.forEach(menu -> {
			ids.add(menu.get("MENU_ID"));
		});
		return ResultVO.getSuccess(ids);
	}

	@Override
	public ResultVO<Object> checkPath(Map<String, Object> data) {
		Condition condition1 = Condition.creatCriteria().andEqual("PATH", data.get("PATH")).andEqual("DELETED",1).build();
		if (baseMapper.count("OS_SYS_MENU", condition1) > 0L) {
			return ResultVO.getFailed("PATH不能重复");
		}
		return ResultVO.getSuccess();
	}

	@Override
	public ResultVO<Object> getOsList() {
		List<Map<String, Object>> osList = baseMapper.selectSql("SELECT OS_ID,OS_NAME FROM OS_SYS_OS WHERE DELETED=1");
		return ResultVO.getSuccess(osList);
	}
}
