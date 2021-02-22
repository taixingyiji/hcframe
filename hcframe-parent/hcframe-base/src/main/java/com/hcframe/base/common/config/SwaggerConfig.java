package com.hcframe.base.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import io.swagger.annotations.Api;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends WebMvcConfigurationSupport {


    @Bean
    @ConfigurationProperties(prefix = "swagger")
    public SwaggerEntity getSwaggerEntity() {
        return new SwaggerEntity();
    }

    @Bean
    public Docket createRestApi() {
        SwaggerEntity swaggerEntity = getSwaggerEntity();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("通用后台模板")
                .description("简单优雅的restfun风格")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }

    /**
     * 防止@EnableMvc把默认的静态资源路径覆盖了，手动设置的方式
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    }

    @Bean
    public FilterRegistrationBean SwaggerFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityBasicAuthFilter());
        registration.addUrlPatterns("/*");
        registration.setName("swaggerSecurityBasic");
        SwaggerEntity swaggerEntity = getSwaggerEntity();
        registration.addInitParameter("enableBasicAuth",swaggerEntity.enableAuth);
        if (SwaggerEntity.TRUE.equals(swaggerEntity.enableAuth)) {
            registration.addInitParameter("userName",swaggerEntity.username);
            registration.addInitParameter("password",swaggerEntity.password);
        }
        return registration;
    }

}
