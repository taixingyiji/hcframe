package com.taixingyiji.base.module.datasource.service;

import com.taixingyiji.base.common.ResultVO;

/**
 * @author lhc
 * @date 2020-09-23
 */
public interface DatasourceConfigService {
    ResultVO add(Integer datasourceConfig);

    ResultVO getRuntimeList();

    ResultVO deleteRuntimeSource(String key);
}
