package com.taixingyiji.base.module.datasource.service;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.github.pagehelper.PageInfo;

/**
 * @author lhc
 * @date 2020-09-23
 */
public interface DataSourceService {
    PageInfo<DatasourceConfig> list(WebPageInfo webPageInfo, DatasourceConfig datasourceConfig);

    ResultVO save(DatasourceConfig datasourceConfig);

    ResultVO update(DatasourceConfig datasourceConfig);

    ResultVO delete(Integer id);

    ResultVO hasSource();

    ResultVO getUnique(String key, String name);

    ResultVO test(DatasourceConfig type);

    ResultVO enableDatasource(Integer id, Integer status);

    ResultVO setDefault(Integer id);

    ResultVO getAllList();

    ResultVO validateToken(String token);
}
