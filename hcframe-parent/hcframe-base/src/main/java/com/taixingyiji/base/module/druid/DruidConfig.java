package com.taixingyiji.base.module.druid;

import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig implements WebMvcConfigurer {


    @ConfigurationProperties(prefix = "druid")
    @Bean
    public DruidAuth auth() {
        return new DruidAuth();
    }

    //配置Druid的监控
    //1、配置一个管理后台的Servlet
    @Bean
    public Object statViewServlet(){
        // Temporary placeholder: original Druid StatViewServlet uses javax.servlet APIs which conflict with Jakarta in Spring Boot 4.
        // Keep a minimal placeholder bean to let the project compile. Restore real Druid servlet integration after migrating to a Jakarta-compatible Druid release.
        return new Object();
    }


    //2、配置一个web监控的filter
    @Bean
    public Object webStatFilter(){
        // Temporary placeholder for the WebStatFilter (same reason as above).
        return new Object();
    }
}
