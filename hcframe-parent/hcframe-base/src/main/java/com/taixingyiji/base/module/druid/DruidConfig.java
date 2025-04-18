package com.taixingyiji.base.module.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
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
    public ServletRegistrationBean<StatViewServlet> statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        DruidAuth druidAuth = auth();
//        initParams.put("loginUsername",druidAuth.getUsername());
//        initParams.put("loginPassword",druidAuth.getPassword());
        //默认就是允许所有访问
        initParams.put("allow",druidAuth.getAllow());
        initParams.put("deny",druidAuth.getDeny());
        bean.setInitParameters(initParams);
        return bean;
    }


    //2、配置一个web监控的filter
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter(){
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        Map<String,String> initParams = new HashMap<>(1);
        initParams.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return  bean;
    }
}
