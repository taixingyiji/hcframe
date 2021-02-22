package com.sinoparasoft.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 配置
 *
 * @author 袁涛
 * @date 2018.06.08
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.sinoparasoft.storage.api")).paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("统一存储API接口文档").description("RESTful风格接口").version("1.0")
				.contact(new Contact("袁涛", "url", "yuan-yu-tao@163.com")).build();
	}
}
