package com.taixingyiji.user.module.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.JudgeException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.Condition;
import com.taixingyiji.base.module.data.module.DataMap;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.user.common.utils.MD5Utils;
import com.taixingyiji.user.module.manage.service.ManageGwService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    BaseMapper baseMapper;
    @Autowired
    TableService tableService;


    @Override
    public ResultVO<Map<String, Object>> addUser(Map<String, Object> user) {
        JudgeException.isNull(user.get("PASSWORD"), "密码不能为空");
        JudgeException.isNull(user.get("LOGIN_NAME"), "用户名不能为空");
        user.put("USER_TYPE", "GW");
        Map<String,Object>  currentUser= (Map<String,Object>)SecurityUtils.getSubject().getPrincipal();
        user.put("ADD_NAME", currentUser.get("NAME"));
        user.put("ADD_LOGIN_NAME", currentUser.get("LOGIN_NAME"));
        if (!StringUtils.isEmpty(user.get("ORG_ACCOUNT_ID"))) {
            String orgAcId = String.valueOf(user.get("ORG_ACCOUNT_ID"));
            user.put("ORG_ACCOUNT_ID", orgAcId.replaceAll("\"", ""));
        }
        if (!StringUtils.isEmpty(user.get("ORG_DEPARTMENT_ID"))) {
            String orgDeptId = String.valueOf(user.get("ORG_DEPARTMENT_ID"));
            user.put("ORG_DEPARTMENT_ID", orgDeptId.replaceAll("\"", ""));
        }
        try {
            user.put("PASSWORD", MD5Utils.encode("Guobo@123"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("新增用户失败", e);
            throw new ServiceException(e);
        }
        user.put("SOURCE", 1);
        return tableService.saveWithDate(GB_CAS_MEMBER, user);
    }

    @Override
    public ResultVO<Map<String,Object>> updateUser(Map<String, Object> user, Integer version) {
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
        return tableService.updateWithDate(GB_CAS_MEMBER, user, version);
    }

    @Override
    public ResultVO<Integer> deleteUser(String ids) {
        return tableService.logicDelete(GB_CAS_MEMBER, ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId) {
        DataMap<Object> dataMap = DataMap.builder().sysOsTable(GB_CAS_MEMBER).build();
        Condition.ConditionBuilder builder = Condition.creatCriteria(dataMap);
        if (!StringUtils.isEmpty(orgId)&&!orgId.equals("guobo")) {
            orgId = orgId.replaceAll("\"", "");
            String sql = "select CODE from GB_CAS_DEPT where CODE like '"+orgId+"%'";
            List<Map<String, Object>> list = baseMapper.selectSql(sql);
            List<Object> idList = new ArrayList<>();
            for (Map<String, Object> code : list) {
                idList.add(code.get("CODE"));
            }
            builder.andIn("DEPT_CODE",idList);
        }
        builder.andEqual("USER_TYPE", "GW");
        if (!StringUtils.isEmpty(data)) {
            try {
                data = URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ServiceException(e);
            }
            JSONArray jsonArray = JSON.parseArray(data);
            builder = tableService.getQueryBuilder(jsonArray, builder);
        }
        builder.andEqual("DELETED", 1);
        PageInfo<Map<String,Object>> page = baseMapper.selectByCondition(builder.build(), webPageInfo);
        List<Map<String,Object>> list =  page.getList();
        for (Map<String, Object> map : list) {
            map.remove("PASSWORD");
            map.put("PASSWORD", "******");
        }
        page.setList(list);
        return ResultVO.getSuccess(page);
    }

    @Override
    public ResultVO<Map<String,Object>> resetPassword(String userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(ID, userId.replaceAll("\"", ""));
        try {
            map.put("PASSWORD", MD5Utils.encode("123456"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败", e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(GB_CAS_MEMBER, map, version);
    }

    @Override
    public ResultVO<Map<String,Object>> disable(Boolean enabled, String userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(ID, userId);
        map.put("DISABLED", enabled);
        return tableService.updateWithDate(GB_CAS_MEMBER, map, version);
    }

    @Override
    public ResultVO<Object> valid(String loginName) {
        Long count = baseMapper.count("GB_CAS_MEMBER", Condition.creatCriteria().andEqual("LOGIN_NAME",loginName).build());
        if (count > 0) {
            return ResultVO.getFailed("用户名已存在");
        }
        return ResultVO.getSuccess();
    }
}
