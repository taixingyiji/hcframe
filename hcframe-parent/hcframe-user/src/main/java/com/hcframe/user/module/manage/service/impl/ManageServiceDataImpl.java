package com.hcframe.user.module.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.DateUtil;
import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.module.data.constants.FieldConstants;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.module.DataMap;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.common.utils.MD5Utils;
import com.hcframe.user.common.utils.POIUtil;
import com.hcframe.user.module.manage.mapper.ManageMapper;
import com.hcframe.user.module.manage.service.ManageService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author lhc
 * @date 2021-02-05
 */
@Service
public class ManageServiceDataImpl implements ManageService {

    private final static Logger logger = LoggerFactory.getLogger(ManageServiceDataImpl.class);

    private static final String PK_ID = "ID";
    private static final String TABLE_NAME = "GB_CAS_MEMBER";
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(TABLE_NAME).tablePk(PK_ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;

    final ManageMapper manageMapper;

    final POIUtil poiUtil;


    public ManageServiceDataImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                                 TableService tableService,
                                 ManageMapper manageMapper,
                                 POIUtil poiUtil) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
        this.manageMapper = manageMapper;
        this.poiUtil = poiUtil;
    }

    @Override
    public ResultVO<Map<String, Object>> addUser(Map<String, Object> user) {
        JudgeException.isNull(user.get("PASSWORD"), "密码不能为空");
        JudgeException.isNull(user.get("LOGIN_NAME"), "用户名不能为空");
        if (!StringUtils.isEmpty(user.get("ORG_ACCOUNT_ID"))) {
            String orgAcId = String.valueOf(user.get("ORG_ACCOUNT_ID"));
            user.put("ORG_ACCOUNT_ID", orgAcId.replaceAll("\"", ""));
        }
        if (!StringUtils.isEmpty(user.get("ORG_DEPARTMENT_ID"))) {
            String orgDeptId = String.valueOf(user.get("ORG_DEPARTMENT_ID"));
            user.put("ORG_DEPARTMENT_ID", orgDeptId.replaceAll("\"", ""));
        }
        try {
            user.put("PASSWORD", MD5Utils.encode((String) user.get("PASSWORD")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("新增用户失败", e);
            throw new ServiceException(e);
        }
        return tableService.saveWithDate(TABLE_INFO, user);
    }

    @Override
    public ResultVO<Integer> updateUser(Map<String, Object> user, Integer version) {
        user.remove("PASSWORD");
        if (!StringUtils.isEmpty(user.get("ORG_ACCOUNT_ID"))) {
            String orgAcId = String.valueOf(user.get("ORG_ACCOUNT_ID"));
            user.put("ORG_ACCOUNT_ID", orgAcId.replaceAll("\"", ""));
        }
        if (!StringUtils.isEmpty(user.get("ORG_DEPARTMENT_ID"))) {
            String orgDeptId = String.valueOf(user.get("ORG_DEPARTMENT_ID"));
            user.put("ORG_DEPARTMENT_ID", orgDeptId.replaceAll("\"", ""));
        }
        return tableService.updateWithDate(TABLE_INFO, user, version);
    }

    @Override
    public ResultVO<Integer> deleteUser(String ids) {
        return tableService.logicDelete(TABLE_INFO, ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId) {
        DataMap<Object> dataMap = DataMap.builder().sysOsTable(TABLE_INFO).build();
        Condition.ConditionBuilder builder = Condition.creatCriteria(dataMap);
        if (!StringUtils.isEmpty(orgId) && !orgId.equals("guobo")) {
            orgId = orgId.replaceAll("\"", "");
            String sql = "select CODE from GB_CAS_DEPT where CODE like '" + orgId + "%'";
            List<Map<String, Object>> list = baseMapper.selectSql(sql);
            List<Object> idList = new ArrayList<>();
            for (Map<String, Object> code : list) {
                idList.add(code.get("CODE"));
            }
            builder.andIn("DEPT_CODE", idList);
        }
        builder.andEqual("USER_TYPE", "GN");
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
        PageInfo<Map<String, Object>> page = baseMapper.selectByCondition(builder.build(), webPageInfo);
        List<Map<String, Object>> list = page.getList();
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
        map.put(PK_ID, userId.replaceAll("\"", ""));
        try {
            map.put("PASSWORD", MD5Utils.encode("123456"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败", e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(TABLE_INFO, map, version);
    }

    @Override
    public ResultVO<Integer> disable(Boolean enabled, String userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(PK_ID, userId);
        map.put("DISABLED", enabled);
        return tableService.updateWithDate(TABLE_INFO, map, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> sync() {
        Workbook workbook = POIUtil.getWorkBook("/Volumes/DATA/2021年3月花名册.xlsx");
        Sheet sheet = workbook.getSheet("Sheet1");
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(1);
            String department = cell.getStringCellValue();
            cell = row.getCell(2);
            String name = cell.getStringCellValue();
            cell = row.getCell(3);
            String date = String.valueOf(cell.getNumericCellValue());
            cell = row.getCell(4);
            String title = cell.getStringCellValue();
            List<Map<String, Object>> list = manageMapper.selectPersonList(name, department);
            if (list != null && list.size() > 0) {
                System.out.println(name + ":" + list.get(0).get("ID"));
                System.out.println(name + ":" + list.get(0).get("ID"));
                String str = date.substring(date.indexOf(".") + 1, date.length());
                if (str.equals("1")) {
                    date = date + "0";
                }
                System.out.println(date.trim() + ".01 00:00:00");
                Date date1 = DateUtil.StringFormat(date.trim() + ".01 00:00:00", "yyyy.MM.dd HH:mm:ss");
                for (Map<String, Object> map : list) {
                    map.put("TIME_TO_WORK", date1);
                    map.put("TITLE_NAME", title);
                    baseMapper.updateByPk(TABLE_INFO, map);
                }
            }
        }
        return null;
    }

    @Override
    public ResultVO<Integer> changePassword(String pwd, String npwd, String npwd2) {
        JudgeException.isNull(pwd, "密码不能为空");
        JudgeException.isNull(npwd, "新密码不能为空");
        if (!npwd.equals(npwd2)) {
            return ResultVO.getFailed("两次新密码输入不一致");
        }
        Map<String, Object> user = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
        String id = (String) user.get("ID");
        Map<String, Object> data = baseMapper.selectByPk(TABLE_NAME, PK_ID, id);
        Integer version = Integer.parseInt(data.get(FieldConstants.VERSION.toString()).toString());
        try {
            if (!data.get("PASSWORD").equals(MD5Utils.encode(pwd))) {
                return ResultVO.getFailed("原密码错误");
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("验证密码失败", e);
            throw new ServiceException(e);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put(PK_ID, id);
        try {
            map.put("PASSWORD", MD5Utils.encode(npwd));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败", e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(TABLE_INFO, map, version);
    }

    @Override
    public ResultVO<Object> getUserPost(String userId) {
        Condition condition = Condition.creatCriteria().andEqual("MEMBER_ID",userId.replaceAll("\"","")).build();
        return ResultVO.getSuccess(baseMapper.selectByCondition("GB_DEPUTY_POST", condition));
    }
}
