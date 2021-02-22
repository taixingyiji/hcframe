package com.hcframe.base.module.datasource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.utils.ObjectUtil;
import com.hcframe.base.module.datasource.dao.DatasourceConfigDao;
import com.hcframe.base.module.datasource.dao.DatasourceTypeDao;
import com.hcframe.base.module.datasource.dynamic.DBContextHolder;
import com.hcframe.base.module.datasource.entity.DatasourceConfig;
import com.hcframe.base.module.datasource.service.DataSourceService;
import com.hcframe.base.module.datasource.service.DatasourceConfigService;
import com.hcframe.base.module.datasource.utils.DataSourceUtil;
import com.hcframe.base.module.datasource.utils.DataUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lhc
 * @date 2020-09-23
 */
@Service
public class DatasourceConfigServiceImpl implements DatasourceConfigService {

    @Autowired
    DatasourceTypeDao datasourceTypeDao;

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    DatasourceConfigDao datasourceConfigDao;

    @Override
    public ResultVO add(Integer id) {
        DatasourceConfig datasourceConfig = datasourceConfigDao.selectByPrimaryKey(id);
        DruidDataSource druidDataSource = DataSourceUtil.initDruid(datasourceConfig.getCommonType());
        BeanUtils.copyProperties(datasourceConfig,druidDataSource);
        //添加数据源到map
        DataSourceUtil.addMapData(datasourceConfig.getCommonAlias(), druidDataSource,datasourceConfig);
        //刷新
        DataSourceUtil.flushDataSource();
        return ResultVO.getSuccess();
    }

    @Override
    public ResultVO getRuntimeList() {
        Map<Object,Object> map = DataSourceUtil.configSourceMap;
        List<Object> list = new ArrayList<>();
        String key = "";
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (DataUnit.MASTER.equals(entry.getKey())) {
                DatasourceConfig datasourceConfig = (DatasourceConfig) entry.getValue();
                key = datasourceConfig.getCommonAlias();
            } else {
                list.add(entry.getValue());
            }
        }
        List<Object> resultList = new ArrayList<>();
        for (Object o : list) {
            DatasourceConfig datasourceConfig = (DatasourceConfig) o;
            if (key.equals(datasourceConfig.getCommonAlias())) {
                datasourceConfig.setIsDefault(DatasourceConfig.DEFAULT);
            } else {
                datasourceConfig.setIsDefault(DatasourceConfig.UNDEFAULT);
            }
            Map<String, Object> tempMap = ObjectUtil.objToMap(datasourceConfig);
            DBContextHolder.setDataSource(DataUnit.SQLITE);
            try {
                ResultVO resultVO = dataSourceService.test(datasourceConfig);
                if (resultVO.getCode() == 0) {
                    tempMap.put("status", 1);
                } else {
                    tempMap.put("status", 0);
                }
            } catch (Exception e) {
                tempMap.put("status", 0);
            }
            resultList.add(tempMap);
        }
        return ResultVO.getSuccess(resultList);
    }

    @Override
    public ResultVO deleteRuntimeSource(String key) {
        DataSourceUtil.removeSource(key);
        DataSourceUtil.flushDataSource();
        return ResultVO.getSuccess();
    }


}
