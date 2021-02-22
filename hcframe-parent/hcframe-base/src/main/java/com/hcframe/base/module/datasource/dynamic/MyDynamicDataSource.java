package com.hcframe.base.module.datasource.dynamic;

import com.alibaba.druid.util.StringUtils;
import com.hcframe.base.module.datasource.utils.DataUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author lhc
 * @date 2020-09-23
 * @description 重写determineCurrentLookupKey方法，通过DBContextHolder获取数据源key值
 */
public class MyDynamicDataSource extends AbstractRoutingDataSource {

    private final Logger logger = LoggerFactory.getLogger(MyDynamicDataSource.class);

    @Override
    public Object determineCurrentLookupKey() {
        //获取当前线程的数据源，如果不存在使用master数据源
        String datasource = DBContextHolder.getDataSource();
        if (StringUtils.isEmpty(datasource)) {
            datasource = DataUnit.MASTER;
        }
        logger.info("datasource=" + datasource);
        return datasource;
    }
}
