package com.hcframe.base.module.tableconfig.service.impl;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.MyPageHelper;
import com.hcframe.base.module.datasource.entity.DatasourceConfig;
import com.hcframe.base.module.tableconfig.dao.OsSysFieldDao;
import com.hcframe.base.module.tableconfig.dao.OsSysTableMapper;
import com.hcframe.base.module.tableconfig.entity.FieldInfo;
import com.hcframe.base.module.tableconfig.entity.OsSysField;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.base.module.tableconfig.entity.TableInfo;
import com.hcframe.base.module.tableconfig.service.TableGenService;
import com.hcframe.base.module.tableconfig.sqlinfo.SqlInfo;
import com.hcframe.base.module.tableconfig.sqlinfo.SqlInfoFactory;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lhc
 * @date 2020-10-16
 * @description 动态生成数据库信息Service实现类
 */
@Service
public class TableGenServiceImpl implements TableGenService {


    @Autowired
    OsSysTableMapper osSysTableMapper;

    @Autowired
    OsSysFieldDao osSysFieldDao;

    @Override
    public ResultVO getTableList(WebPageInfo webPageInfo, TableInfo tableInfo,
                                 DatasourceConfig datasourceConfig) {
        MyPageHelper.start(webPageInfo);
        SqlInfo sqlInfo = SqlInfoFactory.getInstance().get(datasourceConfig.getCommonType());
        List<TableInfo> list = sqlInfo.getTableList(tableInfo, datasourceConfig.getSchemaName());
        return ResultVO.getSuccess(new PageInfo<>(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO genTable(DatasourceConfig datasourceConfig) {
        SqlInfo sqlInfo = SqlInfoFactory.getInstance().get(datasourceConfig.getCommonType());
        List<TableInfo> list = sqlInfo.getTableList(null, datasourceConfig.getSchemaName());
        for (TableInfo tableInfo : list) {
            OsSysTable osSysTable = osSysTableMapper
                    .selectOne(OsSysTable.builder().tableName(tableInfo.getTableName()).build());
            if (osSysTable != null) {
                osSysTable = new OsSysTable();
                String tableName = tableInfo.getTableName();
                osSysTable.setTableName(tableName);
                String tableAlias = tableName.replaceAll("_", "");
                tableAlias = tableAlias.toLowerCase();
                osSysTable.setTableAlias(tableAlias);
                osSysTable.setTableName(tableName);
                osSysTable.setTableContent(tableInfo.getTableComment());
                String primaryKey = sqlInfo.getPrimaryKey(tableName,datasourceConfig.getSchemaName());
                osSysTable.setTablePk(primaryKey);
                int i = osSysTableMapper.insertSelective(osSysTable);
                if (i < 1) {
                    throw new ServiceException("生成表格信息失败");
                }
            }
        }
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO genAllSequence() {
        // TODO 生成Sequence
        return null;
    }

    @Override
    public ResultVO genAll(DatasourceConfig datasourceConfig) {
        // TODO 生成全部表信息
        return null;
    }

    @Override
    public ResultVO getFieldList(WebPageInfo webPageInfo, FieldInfo fieldInfo,
                                 DatasourceConfig datasourceConfig, String tableName) {
        MyPageHelper.start(webPageInfo);
        SqlInfo sqlInfo = SqlInfoFactory.getInstance().get(datasourceConfig.getCommonType());
        List<FieldInfo> list = sqlInfo.getFieldList(datasourceConfig.getSchemaName(), tableName, fieldInfo);
        return ResultVO.getSuccess(new PageInfo<>(list));
    }

    @Override
    public ResultVO genField(DatasourceConfig datasourceConfig, String tableAlias) {
        // TODO 生成字段
        OsSysTable osSysTable = osSysTableMapper.selectOne(OsSysTable.builder().tableAlias(tableAlias).build());
        SqlInfo sqlInfo = SqlInfoFactory.getInstance().get(datasourceConfig.getCommonType());
        List<FieldInfo> list = sqlInfo
                .getFieldList(datasourceConfig.getSchemaName(), osSysTable.getTableName(), new FieldInfo());
        for (FieldInfo fieldInfo : list) {
            List<OsSysField> osSysFieldList = osSysFieldDao
                    .select(OsSysField.builder().fieldName(fieldInfo.getColumnName()).tableId(osSysTable.getTableId()).build());
            if (osSysFieldList == null || osSysFieldList.size() == 0) {
                OsSysField osSysField = new OsSysField();
                osSysField.setFieldName(fieldInfo.getColumnName());
                osSysField.setNameCn(fieldInfo.getColumnComment());
            }
        }
        return null;
    }

}
