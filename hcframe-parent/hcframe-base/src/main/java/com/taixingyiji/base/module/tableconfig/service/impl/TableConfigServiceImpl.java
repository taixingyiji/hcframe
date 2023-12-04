package com.taixingyiji.base.module.tableconfig.service.impl;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.MyPageHelper;
import com.taixingyiji.base.module.cache.CacheService;
import com.taixingyiji.base.module.cache.emum.CacheType;
import com.taixingyiji.base.module.data.exception.SqlException;
import com.taixingyiji.base.module.data.module.BaseMapper;
import com.taixingyiji.base.module.data.module.BaseMapperImpl;
import com.taixingyiji.base.module.data.module.DataMap;
import com.taixingyiji.base.module.tableconfig.dao.OsSysFieldDao;
import com.taixingyiji.base.module.tableconfig.dao.OsSysSelectDao;
import com.taixingyiji.base.module.tableconfig.dao.OsSysTableMapper;
import com.taixingyiji.base.module.tableconfig.entity.OsSysField;
import com.taixingyiji.base.module.tableconfig.entity.OsSysSelect;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.base.module.cache.impl.TableCache;
import com.taixingyiji.base.module.tableconfig.service.TableConfigService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class TableConfigServiceImpl implements TableConfigService {

    @Autowired
    OsSysTableMapper osSysTableMapper;

    @Autowired
    OsSysFieldDao osSysFieldDao;

    @Autowired
    OsSysSelectDao osSysSelectDao;

    @Autowired
    @Qualifier(BaseMapperImpl.BASE)
    BaseMapper baseMapper;

    @Autowired
    @Qualifier(TableCache.TABLE)
    CacheService tableCache;

    @Override
    public ResultVO getTableInfo(OsSysTable osSysTable, WebPageInfo webPageInfo) {
        Example example = new Example(OsSysTable.class);
        Example.Criteria criteria = example.createCriteria();
        MyPageHelper.start(webPageInfo);
        if (!StringUtils.isEmpty(osSysTable.getTableName())) {
            criteria = criteria.andLike("tableName", "%" + osSysTable.getTableName() + "%");
        }
        if (!StringUtils.isEmpty(osSysTable.getTableAlias())) {
            criteria = criteria.andLike("tableAlias", "%" + osSysTable.getTableAlias() + "%");
        }
        if (!StringUtils.isEmpty(osSysTable.getTablePk())) {
            criteria = criteria.andLike("tablePk", "%" + osSysTable.getTablePk() + "%");
        }
        if (!StringUtils.isEmpty(osSysTable.getTableContent())) {
            criteria.andLike("tableContent", "%" + osSysTable.getTableContent() + "%");
        }
        List<OsSysTable> list = osSysTableMapper.selectByExample(example);
        return ResultVO.getSuccess(new PageInfo<>(list));
    }

    @Override
    @Transactional
    public ResultVO saveTableInfo(OsSysTable osSysTable) {
        Example example = new Example(OsSysTable.class);
        example.createCriteria().orEqualTo("tableAlias", osSysTable.getTableAlias());
        List<OsSysTable> list = osSysTableMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return ResultVO.getFailed("表别名重复，请修改后提交！");
        }
        baseMapper.save(osSysTable);
        tableCache.save(CacheType.tableCache, osSysTable.getTableAlias(), osSysTable, OsSysTable.class);
        return ResultVO.getSuccess(osSysTable);
    }


    @Override
    @Transactional
    public ResultVO deleteTable(String ids) {
        String[] idArr = ids.split(",");
        Example queryExample = new Example(OsSysTable.class);
        queryExample.createCriteria().andIn("tableId", Arrays.asList(idArr));
        List<OsSysTable> osSysTables = osSysTableMapper.selectByExample(queryExample);
        for (OsSysTable osSysTable : osSysTables) {
            tableCache.delete(osSysTable.getTableAlias());
        }
        Example fieldExample = new Example(OsSysField.class);
        fieldExample.createCriteria().andIn("tableId", Arrays.asList(idArr));
        List<OsSysField> osSysFields = osSysFieldDao.selectByExample(fieldExample);
        if (osSysFields != null && osSysFields.size() > 0) {
            List<Integer> fieldIds = new ArrayList<>();
            for (OsSysField osSysField : osSysFields) {
                fieldIds.add(osSysField.getFieldId());
            }
            Example selectExample = new Example(OsSysSelect.class);
            selectExample.createCriteria().andIn("fieldId", fieldIds);
            osSysSelectDao.deleteByExample(selectExample);
            osSysFieldDao.deleteByExample(fieldExample);
        }
        osSysTableMapper.deleteByExample(queryExample);
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO updateTable(OsSysTable osSysTable) {
        tableCache.delete(osSysTable.getTableAlias());
        List<OsSysTable> osSysTables = osSysTableMapper.select(OsSysTable.builder().tableAlias(osSysTable.getTableAlias()).build());
        if (osSysTables.size() > 1) {
            return ResultVO.getFailed("表别名设置重复");
        } else {
            if (osSysTables.size() == 1) {
                if (!osSysTables.get(0).getTableId().equals(osSysTable.getTableId())) {
                    return ResultVO.getFailed("表别名设置重复");
                }
            }
        }
        int i = osSysTableMapper.updateByPrimaryKeySelective(osSysTable);
        SqlException.operation(i, "更新表格信息失败");
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO getFieldList(OsSysField osSysField, WebPageInfo webPageInfo) {
        MyPageHelper.start(webPageInfo);
        Example example = new Example(OsSysField.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("tableId", osSysField.getTableId());
        if (!StringUtils.isEmpty(osSysField.getFieldName())) {
            criteria = criteria.andLike("fieldName", "%" + osSysField.getFieldName() + "%");
        }
        if (!StringUtils.isEmpty(osSysField.getNameCn())) {
            criteria = criteria.andLike("nameCn", "%" + osSysField.getNameCn() + "%");
        }
        if (!StringUtils.isEmpty(osSysField.getJavaField())) {
            criteria = criteria.andLike("javaField", "%" + osSysField.getJavaField() + "%");
        }
        if (!StringUtils.isEmpty(osSysField.getJavaType())) {
            criteria = criteria.andEqualTo("javaType", osSysField.getJavaType());
        }
        if (!StringUtils.isEmpty(osSysField.getWebType())) {
            criteria.andEqualTo("webType", osSysField.getWebType());
        }
        List<OsSysField> list = osSysFieldDao.selectByExample(example);
        return ResultVO.getSuccess(new PageInfo<>(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO saveFieldInfo(OsSysField osSysField) {
        DataMap<OsSysField> dataMap = DataMap.<OsSysField>builder().obj(osSysField).build();
        baseMapper.save(dataMap);
        OsSysTable osSysTable = osSysTableMapper.selectOne(OsSysTable.builder().tableId(osSysField.getTableId()).build());
        tableCache.delete(osSysTable.getTableAlias());
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO updateFieldInfo(OsSysField osSysField) {
        int i = osSysFieldDao.updateByPrimaryKeySelective(osSysField);
        SqlException.operation(i, "修改字段信息失败");
        OsSysTable osSysTable = osSysTableMapper.selectOne(OsSysTable.builder().tableId(osSysField.getTableId()).build());
        tableCache.delete(osSysTable.getTableAlias());
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional
    public ResultVO deleteField(String ids) {
        String[] idArr = ids.split(",");
        Example fieldExample = new Example(OsSysField.class);
        fieldExample.createCriteria().andIn("fieldId", Arrays.asList(idArr));
        Example selectExample = new Example(OsSysField.class);
        selectExample.createCriteria().andIn("fieldId", Arrays.asList(idArr));
        List<String> list = osSysFieldDao.getTableAlise(ids);
        for (String tableAlise : list) {
            tableCache.delete(tableAlise);
        }
        int i = osSysFieldDao.deleteByExample(fieldExample);
        i = osSysSelectDao.deleteByExample(selectExample);
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO getSelectList(OsSysSelect osSysSelect) {
        List<OsSysSelect> list = osSysSelectDao.select(osSysSelect);
        return ResultVO.getSuccess(list);
    }

    @Override
    public ResultVO saveSelectInfo(OsSysSelect osSysSelect) {
        DataMap<OsSysSelect> dataMap = DataMap.<OsSysSelect>builder().obj(osSysSelect).build();
        baseMapper.save(dataMap);
        List<String> list = osSysFieldDao.getTableAlise(osSysSelect.getFieldId().toString());
        for (String tableAlise : list) {
            tableCache.delete(tableAlise);
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO updateSelectInfo(OsSysSelect osSysSelect) {
        int i = osSysSelectDao.updateByPrimaryKeySelective(osSysSelect);
        if (i > 0) {
            List<String> list = osSysFieldDao.getTableAlise(osSysSelect.getFieldId().toString());
            for (String tableAlise : list) {
                tableCache.delete(tableAlise);
            }
            return ResultVO.getSuccess();
        } else {
            return ResultVO.getFailed("更新下拉选项失败");
        }
    }

    @Override
    public ResultVO deleteSelectInfo(String ids) {
        Example example = new Example(OsSysSelect.class);
        example.createCriteria().andIn("selectId", Arrays.asList(ids.split(",")));
        List<OsSysSelect> selects = osSysSelectDao.selectByExample(example);
        int i = osSysSelectDao.deleteByExample(example);
        if (i > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (OsSysSelect osSysSelect : selects) {
                stringBuilder.append(osSysSelect.getFieldId()).append(",");
            }
            if (selects.size() > 0) {
                List<String> list = osSysFieldDao.getTableAlise(stringBuilder.substring(0,stringBuilder.length()-1));
                for (String tableAlise : list) {
                    tableCache.delete(tableAlise);
                }
            }
            return ResultVO.getSuccess();
        } else {
            return ResultVO.getFailed("删除下拉选项失败");
        }
    }

    @Override
    public ResultVO getTableSelect() {
        List<OsSysTable> list = osSysTableMapper.selectAll();
        List<Map<String, Object>> resultList = new LinkedList<>();
        for (OsSysTable osSysTable : list) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", osSysTable.getTableId());
            map.put("label", osSysTable.getTableContent());
            resultList.add(map);
        }
        return ResultVO.getSuccess(resultList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO fieldSort(Integer tableId) {
        MyPageHelper.orderBy("ORDER_ID","ASC");
        List<OsSysField> list = osSysFieldDao.select(OsSysField.builder().tableId(tableId).build());
        for (int i = 0; i < list.size(); i++) {
            OsSysField osSysField = list.get(i);
            osSysField.setOrderId(i);
            osSysFieldDao.updateByPrimaryKey(osSysField);
        }
        OsSysTable osSysTable = osSysTableMapper.selectByPrimaryKey(OsSysTable.builder().tableId(tableId).build());
        tableCache.delete(osSysTable.getTableAlias());
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO upMove(Integer id) {
        OsSysField osSysField = osSysFieldDao.selectOne(OsSysField.builder().fieldId(id).build());
        Integer orderId = osSysField.getOrderId();
        OsSysField field = osSysFieldDao.selectOne(OsSysField.builder().tableId(osSysField.getTableId()).orderId(orderId-1).build());
        if (field == null) {
            return ResultVO.getSuccess();
        }
        osSysField.setOrderId(orderId - 1);
        osSysFieldDao.updateByPrimaryKey(osSysField);
        field.setOrderId(orderId);
        osSysFieldDao.updateByPrimaryKey(field);
        OsSysTable osSysTable = osSysTableMapper.selectByPrimaryKey(OsSysTable.builder().tableId(field.getTableId()).build());
        tableCache.delete(osSysTable.getTableAlias());
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO downMove(Integer id) {
        OsSysField osSysField = osSysFieldDao.selectOne(OsSysField.builder().fieldId(id).build());
        Integer orderId = osSysField.getOrderId();
        OsSysField field = osSysFieldDao.selectOne(OsSysField.builder().tableId(osSysField.getTableId()).orderId(orderId+1).build());
        if (field == null) {
            return ResultVO.getSuccess();
        }
        osSysField.setOrderId(orderId + 1);
        osSysFieldDao.updateByPrimaryKey(osSysField);
        field.setOrderId(orderId);
        osSysFieldDao.updateByPrimaryKey(field);
        OsSysTable osSysTable = osSysTableMapper.selectByPrimaryKey(OsSysTable.builder().tableId(field.getTableId()).build());
        tableCache.delete(osSysTable.getTableAlias());
        return ResultVO.getSuccess();
    }
}
