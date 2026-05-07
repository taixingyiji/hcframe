package com.taixingyiji.base.common.config;

import com.taixingyiji.base.common.CamelToDbMapRequestParamResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiger implements WebMvcConfigurer {
    private final CamelToDbMapRequestParamResolver resolver;

    public WebMvcConfiger(CamelToDbMapRequestParamResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(resolver);
    }
}
