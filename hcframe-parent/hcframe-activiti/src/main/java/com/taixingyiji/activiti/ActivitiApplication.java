package com.taixingyiji.activiti;

import com.taixingyiji.base.module.datasource.config.DataSourceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.servlet.annotation.WebFilter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication

@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSwagger2
@EnableCaching
//@EnableDiscoveryClient
//@EnableFeignClients
@MapperScan(basePackages = "com.taixingyiji.**.dao")
@ComponentScan(basePackages = {"com.taixingyiji.**"},excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DataSourceConfiguration.class}))
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }
}
