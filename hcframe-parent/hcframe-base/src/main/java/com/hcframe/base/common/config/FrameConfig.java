package com.hcframe.base.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "frame")
public class FrameConfig {
    private Boolean isRedisLogin = false;
    private Integer loginTimeout = 4;
    private Boolean showControllerLog = true;
}
