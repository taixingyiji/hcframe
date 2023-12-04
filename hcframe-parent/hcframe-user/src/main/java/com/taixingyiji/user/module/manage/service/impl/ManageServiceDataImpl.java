package com.taixingyiji.user.module.manage.service.impl;

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
import com.taixingyiji.user.common.utils.MD5Utils;
import com.taixingyiji.user.common.utils.POIUtil;
import com.taixingyiji.user.module.config.entity.UserConfig;
import com.taixingyiji.user.module.config.service.UserConfigService;
import com.taixingyiji.user.module.manage.mapper.ManageMapper;
import com.taixingyiji.user.module.manage.service.ManageService;
import com.taixingyiji.user.module.userinfo.service.OrgService;
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

    final OrgService orgService;

    final UserConfigService userConfigService;

    public ManageServiceDataImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                                 TableService tableService,
                                 ManageMapper manageMapper,
                                 POIUtil poiUtil,
                                 OrgService orgService,
                                 UserConfigService userConfigService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
        this.manageMapper = manageMapper;
        this.poiUtil = poiUtil;
        this.orgService = orgService;
        this.userConfigService = userConfigService;
    }

    @Override
    public ResultVO<Map<String, Object>> addUser(Map<String, Object> user) {
        JudgeException.isNull(user.get("PASSWORD"), "密码不能为空");
        JudgeException.isNull(user.get("LOGIN_NAME"), "用户名不能为空");
        user.remove("DEPUTY_POST");
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
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        return tableService.saveWithDate(osSysTable, user);
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
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        return tableService.updateWithDate(osSysTable, user, version);
    }

    @Override
    public ResultVO<Integer> deleteUser(String ids) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        return tableService.logicDelete(osSysTable, ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo, String orgId) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        DataMap<Object> dataMap = DataMap.builder().sysOsTable(osSysTable).build();
        Condition.ConditionBuilder builder = Condition.creatCriteria(dataMap);
        if (!StringUtils.isEmpty(orgId)) {
            Long parentId = Long.valueOf(orgId.replaceAll("\"", ""));
            builder.andIn(userConfig.getOrgUserCode(), orgService.getOrgChildIdList(parentId));
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
//        page.setList(list);
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
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        return tableService.updateWithDate(osSysTable, map, version);
    }

    @Override
    public ResultVO<Integer> disable(Boolean enabled, String userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(PK_ID, userId);
        map.put("DISABLED", enabled);
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        return tableService.updateWithDate(osSysTable, map, version);
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
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tablePk(userConfig.getUserKey()).tableName(userConfig.getUserTableName()).build();
        Map<String, Object> user = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
        String id = (String) user.get("ID");
        Map<String, Object> data = baseMapper.selectByPk(osSysTable.getTableName(), osSysTable.getTablePk(), id);
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
        map.put(userConfig.getUserKey(), id);
        try {
            map.put("PASSWORD", MD5Utils.encode(npwd));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败", e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(osSysTable, map, version);
    }

    @Override
    public ResultVO<Object> getUserPost(@org.jetbrains.annotations.NotNull String userId) {
        Condition condition = Condition.creatCriteria().andEqual("MEMBER_ID",userId.replaceAll("\"","")).build();
        return ResultVO.getSuccess(baseMapper.selectByCondition("GB_DEPUTY_POST", condition));
    }
}
