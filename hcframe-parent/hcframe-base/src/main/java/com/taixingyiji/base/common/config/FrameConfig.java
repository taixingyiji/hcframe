package com.taixingyiji.base.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hcframe.config")
public class FrameConfig {
    private Boolean isRedisLogin = false;
    private Integer loginTimeout = 4;
    private Boolean showControllerLog = true;
    /**
     * true keeps the original behavior: every controller request logs params and result.
     * Set false to log a lightweight summary for normal requests, and detailed logs only for
     * slow or sampled requests.
     */
    private Boolean controllerLogDetail = true;
    private Boolean controllerLogParams = true;
    private Boolean controllerLogResult = true;
    private Long controllerLogSlowMillis = 300L;
    private Double controllerLogSampleRate = 1D;
    /**
     * Negative value means no truncation. Use a positive value in production to avoid huge logs.
     */
    private Integer controllerLogMaxValueLength = -1;
    private Boolean multiDataSource = true;
    private Boolean cas = false;
    private Boolean singleClientLogin= false;
    private Integer pageMaxCache = 100000;
    private Integer pageCacheTime = 60;
}
