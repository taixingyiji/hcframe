package com.hcframe.user.module.manage.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.common.utils.MD5Utils;
import com.hcframe.user.module.manage.service.ManageGwService;

/**
 * @author wewe
 * @date 2021年4月19日
 * @description 馆外用户管理
 */
@Service
public class ManageGwServiceImpl implements ManageGwService {
	
	private final static Logger logger = LoggerFactory.getLogger(ManageGwServiceImpl.class);
	private static final String ID = "ID";
	private static final OsSysTable GB_CAS_MEMBER = OsSysTable.builder().tableName("GB_CAS_MEMBER").tablePk(ID).build();

    @Autowired BaseMapper baseMapper;
    @Autowired TableService tableService;
    
	@Override
	public ResultVO<Map<String, Object>> addUser(Map<String, Object> user) {
		JudgeException.isNull(user.get("PASSWORD"),"密码不能为空");
        JudgeException.isNull(user.get("LOGIN_NAME"),"用户名不能为空");
        user.put("USER_TYPE", "GW");
        if (!StringUtils.isEmpty(user.get("ORG_ACCOUNT_ID"))) {
            String orgAcId = String.valueOf(user.get("ORG_ACCOUNT_ID"));
            user.put("ORG_ACCOUNT_ID", orgAcId.replaceAll("\"", ""));
        }
        if (!StringUtils.isEmpty(user.get("ORG_DEPARTMENT_ID"))) {
            String orgDeptId = String.valueOf(user.get("ORG_DEPARTMENT_ID"));
            user.put("ORG_DEPARTMENT_ID", orgDeptId.replaceAll("\"", ""));
        }
        try {
            user.put("PASSWORD",MD5Utils.encode((String) user.get("PASSWORD")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("新增用户失败",e);
            throw new ServiceException(e);
        }
        return tableService.saveWithDate(GB_CAS_MEMBER,user);
	}
	@Override
	public ResultVO<Integer> updateUser(Map<String, Object> user, Integer version) {
		user.put("USER_TYPE", "GW");
		user.remove("PASSWORD");
        if (!StringUtils.isEmpty(user.get("ORG_ACCOUNT_ID"))) {
            String orgAcId = String.valueOf(user.get("ORG_ACCOUNT_ID"));
            user.put("ORG_ACCOUNT_ID", orgAcId.replaceAll("\"", ""));
        }
        if (!StringUtils.isEmpty(user.get("ORG_DEPARTMENT_ID"))) {
            String orgDeptId = String.valueOf(user.get("ORG_DEPARTMENT_ID"));
            user.put("ORG_DEPARTMENT_ID", orgDeptId.replaceAll("\"", ""));
        }
        return tableService.updateWithDate(GB_CAS_MEMBER,user,version);
	}
	@Override
	public ResultVO<Integer> deleteUser(String ids) {
		return tableService.logicDelete(GB_CAS_MEMBER,ids);
	}
	@Override
	public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo) {
		PageInfo<Map<String, Object>> page = tableService.searchSingleTables(data, GB_CAS_MEMBER, webPageInfo);
        List<Map<String,Object>> list =  page.getList();
        for (Map<String, Object> map : list) {
            map.remove("PASSWORD");
            map.put("PASSWORD", "******");
        }
        page.setList(list);
        return ResultVO.getSuccess(page);
	}
	@Override
	public ResultVO<Integer> resetPassword(String userId, Integer version) {
		Map<String, Object> map = new HashMap<>(2);
        map.put(ID, userId.replaceAll("\"",""));
        try {
            map.put("PASSWORD",MD5Utils.encode("123456"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败",e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(GB_CAS_MEMBER,map,version);
	}
	@Override
	public ResultVO<Integer> disable(Boolean enabled, String userId, Integer version) {
		Map<String, Object> map = new HashMap<>(2);
        map.put(ID, userId);
        map.put("DISABLED", enabled);
        return tableService.updateWithDate(GB_CAS_MEMBER,map,version);
	}
}
