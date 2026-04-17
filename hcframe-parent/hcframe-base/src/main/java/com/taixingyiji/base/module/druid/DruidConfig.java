package com.taixingyiji.base.module.druid;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class DruidConfig implements WebMvcConfigurer {

    @ConfigurationProperties(prefix = "druid")
    @Bean
    public DruidAuth auth() {
        return new DruidAuth();
    }

    @Bean
    public ServletRegistrationBean<Servlet> statViewServlet(DruidAuth auth) {
        ServletRegistrationBean<Servlet> registrationBean =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParameters = new LinkedHashMap<>();
        initParameters.put("loginUsername", auth.getUsername());
        initParameters.put("loginPassword", auth.getPassword());
        initParameters.put("resetEnable", "false");
        if (StringUtils.hasText(auth.getAllow())) {
            initParameters.put("allow", auth.getAllow());
        }
        if (StringUtils.hasText(auth.getDeny())) {
            initParameters.put("deny", auth.getDeny());
        }
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> webStatFilter() {
        FilterRegistrationBean<Filter> registrationBean =
                new FilterRegistrationBean<>(new WebStatFilter());
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));
        registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return registrationBean;
    }
}
