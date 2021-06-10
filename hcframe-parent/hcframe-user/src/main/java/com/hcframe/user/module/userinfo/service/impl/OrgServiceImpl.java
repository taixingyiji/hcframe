package com.hcframe.user.module.userinfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.MyPageHelper;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.module.DataMap;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.redis.RedisUtil;
import com.hcframe.user.common.config.UserConstant;
import com.hcframe.user.module.config.entity.UserConfig;
import com.hcframe.user.module.config.service.UserConfigService;
import com.hcframe.user.module.userinfo.service.OrgService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author lhc
 */
@Service
public class OrgServiceImpl implements OrgService {


    final BaseMapper baseMapper;

    final TableService tableService;

    final UserConfigService userConfigService;

    final RedisUtil redisUtil;

    public OrgServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                          TableService tableService,
                          UserConfigService userConfigService,
                          RedisUtil redisUtil) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
        this.userConfigService = userConfigService;
        this.redisUtil = redisUtil;
    }


    @Override
    public ResultVO<Object> addOrg(Map<String, Object> org) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tableName(userConfig.getOrgTableName()).tablePk(userConfig.getOrgKey()).build();
        return ResultVO.getSuccess(tableService.saveWithDate(osSysTable, org));
    }

    @Override
    public ResultVO<Integer> updateOrg(Map<String, Object> org, Integer version) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tableName(userConfig.getOrgTableName()).tablePk(userConfig.getOrgKey()).build();
        return tableService.updateWithDate(osSysTable, org, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteOrg(String ids) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tableName(userConfig.getOrgTableName()).tablePk(userConfig.getOrgKey()).build();
        tableService.logicDelete(osSysTable, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getOrgList(String data, WebPageInfo webPageInfo, String parentId) {
        UserConfig userConfig = userConfigService.getUserConfig();
        OsSysTable osSysTable = OsSysTable.builder().tableName(userConfig.getOrgTableName()).tablePk(userConfig.getOrgKey()).build();
        DataMap dataMap = DataMap.builder().sysOsTable(osSysTable).build();
        Condition.ConditionBuilder builder = Condition.creatCriteria(dataMap);
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
        if (!StringUtils.isEmpty(parentId)) {
            builder.andIn("ID", getOrgChildIdList(Long.valueOf(parentId.replaceAll("\"", ""))));
        }
        PageInfo<Map<String, Object>> pageInfo = baseMapper.selectByCondition(builder.build(), webPageInfo);
        return ResultVO.getSuccess(pageInfo);
    }

    @Override
    public List<Long> getOrgChildIdList(Long parentId) {
        Set<Long> ids = new HashSet<>();
        getOrgChilidIds(parentId, ids, getOrgTree());
        return new ArrayList<>(ids);
    }

    // 递归获取所有下属ID
    private void getOrgChilidIds(Long parentId, Set<Long> ids, List<Map<String, Object>> treeList) {
        ids.add(parentId);
        Long childParentId;
        for (Map<String, Object> map : treeList) {
            if (map.get("PARENT_ID").equals(parentId)) {
                ids.add((Long) map.get("ID"));
                childParentId = (Long) map.get("ID");
            } else {
                childParentId = parentId;
            }
            if (map.get("children") != null && ((List<Map<String, Object>>) map.get("children")).size() > 0) {
                getOrgChilidIds(childParentId, ids, (List<Map<String, Object>>) map.get("children"));
            }
        }
    }

    @Override
    public List<Map<String, Object>> getOrgTree() {
        List<Map<String, Object>> results = (List<Map<String, Object>>) redisUtil.hget(UserConstant.USER, UserConstant.ORG_TREE);
        UserConfig userConfig = userConfigService.getUserConfig();
        if (results == null) {
            Condition condition = Condition.creatCriteria().andEqual("DELETED", 1).build();
            MyPageHelper.orderBy("SORT_ID", "ASC");
            List<Map<String, Object>> list = baseMapper.selectByCondition(userConfig.getOrgTableName(), condition);
            results = getChildPerms(list, 0, userConfig);
            redisUtil.hset(UserConstant.USER, UserConstant.ORG_TREE, results);
        }
        return results;
    }

    @Override
    public ResultVO<Object> getFormat() {
        UserConfig userConfig = userConfigService.getUserConfig();
        List<Map<String, Object>> list = baseMapper.selectAll(userConfig.getOrgTableName());
        OsSysTable osSysTable = OsSysTable.builder().tableName(userConfig.getOrgTableName()).tablePk(userConfig.getOrgKey()).build();
        for (Map<String, Object> map : list) {
            if (!"guobo".equals(map.get("CODE"))) {
                if (String.valueOf(map.get("CODE")).length() == 4) {
                    map.put("PARENT_ID", -3858082048188003782L);
                    baseMapper.updateByPk(osSysTable, map);
                } else {
                    String parentCode = String.valueOf(map.get("CODE")).substring(0, 4);
                    for (Map<String, Object> parent : list) {
                        if (parentCode.equals(parent.get("CODE"))) {
                            map.put("PARENT_ID", parent.get("ID"));
                            baseMapper.updateByPk(osSysTable, map);
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> getChildPerms(List<Map<String, Object>> list, long parentId, UserConfig userConfig) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        for (Map<String, Object> t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.get(userConfig.getOrgParentId()).equals(parentId)) {
                recursionFn(list, t, userConfig);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<Map<String, Object>> list, Map<String, Object> t, UserConfig userConfig) {
        // 得到子节点列表
        List<Map<String, Object>> childList = getChildList(list, t, userConfig);
        t.put("children", childList);
        for (Map<String, Object> tChild : childList) {
            if (hasChild(list, tChild, userConfig)) {
                recursionFn(list, tChild, userConfig);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Map<String, Object>> getChildList(List<Map<String, Object>> list, Map<String, Object> t, UserConfig userConfig) {
        List<Map<String, Object>> tlist = new ArrayList<>();
        for (Map<String, Object> n : list) {
            if (n.get(userConfig.getOrgParentId()).equals(t.get(userConfig.getOrgKey()))) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    private boolean hasChild(List<Map<String, Object>> list, Map<String, Object> t, UserConfig userConfig) {
        return getChildList(list, t, userConfig).size() > 0;
    }
}
