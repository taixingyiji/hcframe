package com.taixingyiji.base.module.data.module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.ConfigurationCustomizer;

/**
 * @className MyBatisConfig
 * @author lhc
 * @date 2025年03月28日 19:43
 * @description 描述
 * @version 1.0
 */
@Configuration
public class MyBatisConfig {
    @Value("${mybatis.enable-highgo}")
    private boolean customObjectWrapperEnabled;
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            if (customObjectWrapperEnabled) {
                configuration.setObjectWrapperFactory(new UpperCaseMapWrapperFactory());
            }
        };
    }
}
