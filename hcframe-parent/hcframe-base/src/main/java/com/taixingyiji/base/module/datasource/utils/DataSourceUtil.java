package com.taixingyiji.base.module.datasource.utils;

import com.taixingyiji.base.common.ServiceException;
import com.taixingyiji.base.common.utils.SpringContextUtil;
import com.taixingyiji.base.common.utils.TokenProccessor;
import com.taixingyiji.base.module.datasource.dao.DatasourceConfigDao;
import com.taixingyiji.base.module.datasource.dao.DatasourceTokenDao;
import com.taixingyiji.base.module.datasource.dao.DatasourceTypeDao;
import com.taixingyiji.base.module.datasource.dynamic.DBContextHolder;
import com.taixingyiji.base.module.datasource.dynamic.MyDynamicDataSource;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.datasource.entity.DatasourceToken;
import com.taixingyiji.base.module.datasource.entity.DatasourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


/**
 * @author lhc
 * @date 2020-09-23
 * @description 数据源工具类，用来缓存管理数据源信息
 */
@Component
public class DataSourceUtil {

    private static DatasourceConfigDao datasourceConfigDao;

    private static DatasourceTypeDao datasourceTypeDao;

    private static javax.sql.DataSource defaultDataSource;

    private static DatasourceTokenDao datasourceTokenDao;

    @Autowired
    DatasourceConfigDao dMapper;

    @Autowired
    DatasourceTypeDao tMapper;

    @Autowired
    DatasourceTokenDao tokenDao;

    @PostConstruct
    public void init() {
        DataSourceUtil.datasourceConfigDao = dMapper;
        DataSourceUtil.datasourceTypeDao = tMapper;
        DataSourceUtil.datasourceTokenDao = tokenDao;
    }


    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);
    public static final Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();
    public static final Map<Object, Object> configSourceMap = new ConcurrentHashMap<>();


    /**
     * 项目启动初始化数据源
     */
    public static void initDataSource() {
        try {
            //获取masterDataSource
            HikariDataSource masterDataSource = (HikariDataSource) SpringContextUtil.getBean(DataUnit.MASTERBEAN);
            DataSourceUtil.defaultDataSource = (HikariDataSource) SpringContextUtil.getBean(DataUnit.MASTERBEAN);
            addDataSource(DataUnit.MASTER, masterDataSource);
            addDataSource(DataUnit.SQLITE, masterDataSource);
            flushDataSource();
            logger.info("add sqlite success!");
        } catch (Exception e) {
            logger.error("add sqlite error!", e);
        }
        String token = genToken();
        try {
            //初始化其它数据源
            initOthersDataSource();
            //刷新数据源
            flushDataSource();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("add datasource error!");
            logger.error("请检查Sqlite配置是否正确");
        }
        logger.info("数据源Token为：" + token);
    }

    /**
     * 刷新数据源
     */
    public static void flushDataSource() {
        //获取spring管理的dynamicDataSource
        MyDynamicDataSource myDynamicDataSource = (MyDynamicDataSource) SpringContextUtil.getBean(DataUnit.DYNAMICBEAN);
        //将数据源设置到 targetDataSources
        myDynamicDataSource.setTargetDataSources(dataSourceMap);
        //将 targetDataSources 中的连接信息放入 resolvedDataSources 管理
        myDynamicDataSource.afterPropertiesSet();
    }

    public static void addDataSource(String key, javax.sql.DataSource masterDataSource) {
        dataSourceMap.put(key, masterDataSource);
    }

    public static void addMapData(String key, javax.sql.DataSource dataSource, DatasourceConfig datasourceConfig) {
        dataSourceMap.put(key, dataSource);
        datasourceConfig.setPassword(DataUnit.PASSWORD);
        configSourceMap.put(key, datasourceConfig);
    }

    public static DatasourceConfig get(String key) {
        return (DatasourceConfig) configSourceMap.get(key);
    }

    public static void addConfigSource(String key, DatasourceConfig datasourceConfig) {
        configSourceMap.put(key, datasourceConfig);
    }

    public static void removeSource(String key) {
        dataSourceMap.remove(key);
        configSourceMap.remove(key);
    }

    /**
     * 初始化其他数据源
     */
    private static void initOthersDataSource() {
        // 从Sqlite中获取db列表
        List<DatasourceConfig> list = datasourceConfigDao.selectAll();
        if (list != null && list.size() > 0) {
            // 遍历db列表添加到数据源
            list.forEach(datasourceConfig -> {
                if (datasourceConfig.getSysEnabled() == DatasourceConfig.ENABLE) {
                    javax.sql.DataSource dataSource = initHikari(datasourceConfig.getCommonType());
                    BeanUtils.copyProperties(datasourceConfig, dataSource);
                    addMapData(datasourceConfig.getCommonAlias(), dataSource, datasourceConfig);
                    logger.info("add datasource " + datasourceConfig.getSysDescription());
                    if (datasourceConfig.getIsDefault() == DatasourceConfig.DEFAULT) {
                        addMapData(DataUnit.MASTER, dataSource, datasourceConfig);
                    }
                }
            });
        }
        DBContextHolder.clearDataSource();
    }

    /**
     * 初始化Hikari配置
     *
     * @param type
     */
    public static javax.sql.DataSource initHikari(String type) {
        HikariConfig config = new HikariConfig();
        // map common pool properties from defaultDataSource if available
        if (defaultDataSource != null) {
            // try to copy Hikari-specific properties if the default data source is Hikari
            if (defaultDataSource instanceof HikariDataSource) {
                HikariDataSource ds = (HikariDataSource) defaultDataSource;
                try { config.setMaximumPoolSize(ds.getMaximumPoolSize()); } catch (Exception ignored) {}
                try { config.setMinimumIdle(ds.getMinimumIdle()); } catch (Exception ignored) {}
                try { config.setConnectionTimeout(ds.getConnectionTimeout()); } catch (Exception ignored) {}
                try { config.setIdleTimeout(ds.getIdleTimeout()); } catch (Exception ignored) {}
                try { config.setMaxLifetime(ds.getMaxLifetime()); } catch (Exception ignored) {}
            }
        }
        // set validation query if available via datasourceType
        DatasourceType datasourceType = datasourceTypeDao.selectOne(DatasourceType.builder().typeKey(type).build());
        if (datasourceType != null && datasourceType.getValidateQuery() != null) {
            config.setConnectionTestQuery(datasourceType.getValidateQuery());
        }
        // other sensible defaults
        config.setAutoCommit(true);
        config.setPoolName("hcframe-hikari-");

        HikariDataSource hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }

    /**
     * 生成token
     */
    public static String genToken() {
        String token;
        List<DatasourceToken> list = datasourceTokenDao.selectAll();
        if (list == null || list.size() == 0) {
            TokenProccessor tokenProccessor = TokenProccessor.getInstance();
            //生成一个token
            token = tokenProccessor.makeToken();
            DatasourceToken datasourceToken = DatasourceToken.builder().token(token).build();
            int i = datasourceTokenDao.insert(datasourceToken);
            if (i < 1) {
                throw new ServiceException("新增token失败");
            } else {
                return token;
            }
        } else {
            DatasourceToken datasourceToken = list.get(0);
            token = datasourceToken.getToken();
        }
        return token;
    }
}