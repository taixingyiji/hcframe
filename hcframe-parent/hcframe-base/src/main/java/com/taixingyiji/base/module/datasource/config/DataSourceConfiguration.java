package com.taixingyiji.base.module.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.taixingyiji.base.common.MyMapper;
import com.taixingyiji.base.module.datasource.dynamic.MyDynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author lhc
 * @date 2020-09-23
 * @description 配置项目数据源配置类
 */
@Configuration
// 配置tk.mybatis参数及通用mapper,指定sqlSession工厂Bean
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.taixingyiji.**.dao",  //这里扫描dao层的接口
        sqlSessionFactoryRef = "sqlSessionFactoryBean222",
        markerInterface = MyMapper.class   //指定tkmybatis 中的通用mapper
)
// 配置mybatis参数，指定sqlSession工厂Bean
@MapperScan(basePackages = "com.taixingyiji.**.dao",sqlSessionFactoryRef = "sqlSessionFactoryBean222")
@EnableConfigurationProperties(MybatisProperties.class)
public class DataSourceConfiguration {

    private final MybatisProperties properties;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(DataSourceConfiguration.class);

    public DataSourceConfiguration(MybatisProperties properties) {
        this.properties = properties;
    }

    /**
     * @return
     * @Bean 防止数据监控报错，无法查看数据源
     * @ConfigurationProperties 会把配置文件的参数自动赋值到dataSource里。
     * @Primary 用于标识默认使用的 DataSource Bean
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        MyDynamicDataSource myDynamicDataSource = new MyDynamicDataSource();
        // 配置多数据源
        Map<Object, Object> targetDataSources = new HashMap<>(1);
        targetDataSources.put("master", masterDataSource());
        myDynamicDataSource.setTargetDataSources(targetDataSources);
        return myDynamicDataSource;
    }
    /**
     * 配置 SqlSessionFactoryBean
     */
    @Bean(value = "sqlSessionFactoryBean222")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTypeAliasesPackage("com.**.**.entity");
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            sqlSessionFactoryBean.setMapperLocations(this.properties.resolveMapperLocations());
        }
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        return sqlSessionFactoryBean;
    }

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryBean222")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
