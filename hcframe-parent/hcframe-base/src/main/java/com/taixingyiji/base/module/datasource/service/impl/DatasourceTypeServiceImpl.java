package com.taixingyiji.base.module.datasource.service.impl;

import com.taixingyiji.base.module.datasource.dao.DatasourceTypeDao;
import com.taixingyiji.base.module.datasource.entity.DatasourceType;
import com.taixingyiji.base.module.datasource.service.DatasourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasourceTypeServiceImpl implements DatasourceTypeService {

    @Autowired
    DatasourceTypeDao datasourceTypeDao;

    @Override
    public List<DatasourceType> getAllInfo() {
        return datasourceTypeDao.selectAll();
    }
}
