package com.hcframe.user.module.userinfo.service.impl;

import com.github.pagehelper.PageInfo;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import com.hcframe.base.module.data.module.Condition;
import com.hcframe.base.module.data.service.TableService;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.user.module.userinfo.service.DeptService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return ResultVO.getSuccess(tableService.saveWithDate(TABLE_INFO, org));
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
        baseMapper.deleteByCondition(OS_SYS_POSITION, condition);
        tableService.delete(TABLE_INFO, ids);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO<PageInfo<Map<String, Object>>> getDeptList(String data, WebPageInfo webPageInfo) {
        PageInfo<Map<String, Object>> pageInfo = tableService.searchSingleTables(data, TABLE_INFO, webPageInfo);
        return ResultVO.getSuccess(pageInfo);
    }

    @Override
    public ResultVO<List<Map<String, Object>>> getDeptTree() {
        // 获取全部
        String sql = "SELECT " + ID + "," + PATH + "," + TYPE + "," + NAME + " FROM " + GB_CAS_DEPT + " ORDER BY " + SORT_ID;
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
                    String rootMapPath = String.valueOf(path).substring(0, path.length() - 4);
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