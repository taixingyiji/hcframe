package com.hcframe.user.module.manage.service.impl;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.JudgeException;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.common.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.hcframe.user.module.manage.service.ManageService;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public ManageServiceDataImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                                 TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    public ResultVO<Map<String,Object>> addUser(Map<String, Object> user) {
        JudgeException.isNull(user.get("PASSWORD"),"密码不能为空");
        JudgeException.isNull(user.get("LOGIN_NAME"),"用户名不能为空");
        try {
            user.put("PASSWORD",MD5Utils.encode((String) user.get("PASSWORD")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("更新用户失败",e);
            throw new ServiceException(e);
        }
        return tableService.saveWithDate(TABLE_INFO,user);
    }

    @Override
    public ResultVO<Integer> updateUser(Map<String, Object> user, Integer version) {
        user.remove("PASSWORD");
        return tableService.updateWithDate(TABLE_INFO,user,version);
    }

    @Override
    public ResultVO<Integer> deleteUser(String ids) {
        return tableService.delete(TABLE_INFO,ids);
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getUserList(String data, WebPageInfo webPageInfo) {
        PageInfo<Map<String, Object>> page = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
        List<Map<String,Object>> list =  page.getList();
        for (Map<String, Object> map : list) {
            map.remove("PASSWORD");
        }
        page.setList(list);
        return ResultVO.getSuccess(page);
    }

    @Override
    public ResultVO<Integer> resetPassword(Long userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(PK_ID, userId);
        try {
            map.put("PASSWORD",MD5Utils.encode("123456"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("重置密码失败",e);
            throw new ServiceException(e);
        }
        return tableService.updateWithDate(TABLE_INFO,map,version);
    }

    @Override
    public ResultVO<Integer> disable(Boolean enabled, Long userId, Integer version) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(PK_ID, userId);
        map.put("DISABLED", enabled);
        return tableService.updateWithDate(TABLE_INFO,map,version);
    }
}
