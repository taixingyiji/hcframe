package com.taixingyiji.base.common;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.taixingyiji.base.common.utils.MapKeyConvertUtil;
import com.taixingyiji.base.module.data.annotation.CamelToDbMap;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CamelToDbMapRequestParamResolver implements HandlerMethodArgumentResolver {


    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();

        webRequest.getParameterMap().forEach((key, values) -> {
            Object value = values != null && values.length == 1 ? values[0] : values;
            result.put(MapKeyConvertUtil.camelToUpperUnderline(key), value);
        });

        return result;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CamelToDbMap.class)
                && Map.class.isAssignableFrom(parameter.getParameterType());
    }
}
