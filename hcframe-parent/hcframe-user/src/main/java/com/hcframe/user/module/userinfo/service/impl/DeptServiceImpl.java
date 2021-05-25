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
import com.hcframe.user.module.userinfo.service.DeptService;
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
public class DeptServiceImpl implements DeptService {

    private static final String ID = "ID";
    private static final String GB_CAS_DEPT = "GB_CAS_DEPT";
    private static final String OS_SYS_POSITION = "OS_SYS_POSITION";
    private static final String ORG_ACCOUNT_ID = "ORG_ACCOUNT_ID";
    private static final String TYPE = "TYPE";
    private static final String NAME = "NAME";
    private static final String PATH = "PATH";
    private static final String SORT_ID = "SORT_ID";
    private static final String[] LABELCH = {"一级", "二级", "三级", "四级", "五级", "六级", "七级", "八级", "九级", "十级"};
    private static final OsSysTable TABLE_INFO = OsSysTable.builder().tableName(GB_CAS_DEPT).tablePk(ID).build();

    final BaseMapper baseMapper;

    final TableService tableService;


    public DeptServiceImpl(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper,
                           TableService tableService) {
        this.baseMapper = baseMapper;
        this.tableService = tableService;
    }

    @Override
    public ResultVO<Object> addDept(Map<String, Object> org) {
        String parentCode = (String) org.get("PARENT_CODE");
        org.remove("PARENT_CODE");
        String code;
        String path;
        if ("guobo".equals(parentCode)) {
            MyPageHelper.orderBy("CODE", "DESC");
            List<Map<String, Object>> list = baseMapper.selectAll(GB_CAS_DEPT);
            List<Map<String, Object>> pathList = new LinkedList<>();
            List<Map<String, Object>> codeList = new LinkedList<>();
            for (Map<String, Object> map : list) {
                if (4 == map.get("CODE").toString().length()) {
                    codeList.add(map);
                }
            }
            MyPageHelper.orderBy("PATH", "DESC");
            list = baseMapper.selectAll(GB_CAS_DEPT);
            for (Map<String, Object> map : list) {
                if (12 == map.get("PATH").toString().length()) {
                    pathList.add(map);
                }
            }
            code = getCode(codeList,parentCode);
            path = getPath(pathList);
        } else {
            Condition condition = Condition.creatCriteria().andLike("CODE", parentCode + "%").build();
            MyPageHelper.orderBy("CODE", "DESC");
            List<Map<String, Object>> codeList = baseMapper.selectByCondition(GB_CAS_DEPT, condition);
            code = getCode(codeList, parentCode);
            MyPageHelper.orderBy("PATH", "DESC");
            List<Map<String, Object>> pathList = baseMapper.selectByCondition(GB_CAS_DEPT, condition);
            path = getPath(pathList);
        }
        org.put("CODE", code);
        org.put("PATH", path);
        MyPageHelper.start(WebPageInfo.builder().pageNum(1).pageSize(1).sortField("SORT_ID").order("DESC").build());
        List<Map<String, Object>> list = baseMapper.selectAll(GB_CAS_DEPT);
        org.put("SORT_ID", Integer.parseInt(String.valueOf(list.get(0).get("SORT_ID")))+1);
        return ResultVO.getSuccess(tableService.saveWithDate(TABLE_INFO, org));
    }

    private String getCode(List<Map<String,Object>> codeList,String parentCode){
        String tempCode = "";
        String lastCode = (String) codeList.get(0).get("CODE");
        int incrementCode = Integer.parseInt(lastCode.substring(lastCode.length() - 2)) + 1;
        if ("guobo".equals(parentCode)) {
            parentCode = "D";
            if (incrementCode < 10) {
                tempCode = tempCode + "00" + incrementCode;
            } else if (incrementCode < 100) {
                tempCode = tempCode + "0" + incrementCode;
            } else {
                tempCode = tempCode + incrementCode;
            }
        } else {
            if (incrementCode < 10) {
                tempCode = tempCode + "0" + incrementCode;
            } else {
                tempCode = tempCode + incrementCode;
            }
        }
        return parentCode + tempCode;
    }

    private String getPath(List<Map<String,Object>> pathList) {
        String tempPath = "";
        String lastPath = (String) pathList.get(0).get("PATH");
        String parentPath = lastPath.substring(0, lastPath.length() - 4);
        int incrementPath = Integer.parseInt(lastPath.substring(lastPath.length() - 4)) + 1;
        if (pathList.size() == 1) {
            parentPath = lastPath;
            tempPath = "0001";
        } else {
            if (incrementPath < 10) {
                tempPath = tempPath + "000" + incrementPath;
            } else if (incrementPath < 100) {
                tempPath = tempPath + "00" + incrementPath;
            } else if (incrementPath < 1000) {
                tempPath = tempPath + "0" + incrementPath;
            } else {
                tempPath = tempPath + incrementPath;
            }
        }
        return parentPath + tempPath;
    }

    @Override
    public ResultVO<Integer> updateDept(Map<String, Object> org, Integer version) {
        return tableService.updateWithDate(TABLE_INFO, org, version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Object> deleteDept(String ids) {
        String[] idArr = ids.split(",");
        Condition condition = Condition
                .creatCriteria()
                .andIn(ID, Arrays.asList(idArr))
                .build();
        tableService.logicDelete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getDeptList(String data, WebPageInfo webPageInfo, String code) {
        DataMap<Object> dataMap = DataMap.builder().sysOsTable(TABLE_INFO).build();
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
        if (!StringUtils.isEmpty(code)) {
            builder.andLike("CODE", code + "%");
        }
        builder.andEqual("DELETED", 1);
        PageInfo<Map<String, Object>> pageInfo = baseMapper.selectByCondition(builder.build(), webPageInfo);
        ;
        return ResultVO.getSuccess(pageInfo);
    }

    @Override
    public ResultVO<List<Map<String, Object>>> getDeptTree() {
        // 获取全部
        String sql = "SELECT " + ID + "," + PATH + "," + TYPE + "," + NAME + ",CODE FROM " + GB_CAS_DEPT + " ORDER BY " + SORT_ID;
        List<Map<String, Object>> allDataList = baseMapper.selectSql(sql);
        // 根节点
        List<Map<String, Object>> rootList = new ArrayList<>();
        // 遍历list生成以父节点为key的map
        Map<String, List<Map<String, Object>>> pathMap = new HashMap<>();
        for (Map<String, Object> map : allDataList) {
            if (map != null && map.containsKey(PATH)) {
                String path = String.valueOf(map.get(PATH));
                if (path != null && path.length() > 4) {
                    // 获取父节点路径
                    String rootPath = path.substring(0, path.length() - 4);
                    // 看当前orgAccountIdMap中是否有对应key，有则添加到list中，没有则创建list
                    if (pathMap.containsKey(rootPath)) {
                        List<Map<String, Object>> pathMapList = pathMap.get(rootPath);
                        pathMapList.add(map);
                    } else {
                        List<Map<String, Object>> pathMapList = new ArrayList<>();
                        pathMapList.add(map);
                        pathMap.put(rootPath, pathMapList);
                    }
                }
            }
            // 根据map的type判断是否为根节点
            if (map != null && map.containsKey(TYPE)) {
                String type = String.valueOf(map.get(TYPE));
                if ("Account".equalsIgnoreCase(type)) {
                    rootList.add(map);
                }
            }
        }
        handleChildrenMap(pathMap, rootList, "");
        return ResultVO.getSuccess(rootList);
    }


    // 传入节点所有孩子节点，并在总pathMap中寻找孩子节点，为空的时候结束迭代
    private void handleChildrenMap(Map<String, List<Map<String, Object>>> pathMap, List<Map<String, Object>> list, String labelPre) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if (map != null && map.containsKey(PATH)) {
                    String path = String.valueOf(map.get(PATH));
                    // 遍历map添加label信息
                    int index = path.length() / 4 - 2;
                    String labelch = LABELCH[index];
                    int labelIndex = i + 1;
                    map.put("label", labelch + " " + labelPre + labelIndex);
                    map.put("labelNum", index);
                    map.put("labelIndex", labelIndex);
                    // 把rootMap中
                    String rootMapPath = path.substring(0, path.length() - 4);
                    // 从orgAccountIdMap中获取所有pathMap为rootMapId的List取出添加到rootMap下，
                    // 只到某Map的Id在orgAccountId在orgAccountIdMap检索不到为止
                    List<Map<String, Object>> childrenMapList = new ArrayList<>();
                    // 根据当前节点的path寻找pathMap中的内容
                    if (pathMap.containsKey(path)) {
                        childrenMapList = pathMap.get(path);
                        handleChildrenMap(pathMap, childrenMapList, labelPre + labelIndex + "-");
                    }
                    map.put("children", childrenMapList);
                }
            }
        }
    }
}
