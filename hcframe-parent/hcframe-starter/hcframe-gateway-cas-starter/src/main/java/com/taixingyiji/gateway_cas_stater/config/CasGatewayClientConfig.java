package com.taixingyiji.gateway_cas_stater.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lhc
 * @date 2021-03-02
 */
@Data
@ConfigurationProperties("hcframe.cas")
public class CasGatewayClientConfig {

    public Boolean enabled = true;

    public String casServiceUrl;

    public String serviceUrl;

    public String casContextPath="/cas";

    public String clientContextPath;

    public String loginUrl="/login";

    public String logoutUrl="/logout";

    public String whiteUrl="^.*(/logout/?)$";

    public String ignoreUrlPatternType="REGEX";

    public String cookieHolderPattern="com.taixingyiji.gateway_cas_stater.data.DataStorageImpl";

    public String authKey="X-Access-Token";

    public String proxyReceptorUrl;

    public String millisBetweenCleanUps="3600000";

    public Boolean acceptAnyProxy=false;

    public String proxyCallbackUrl;
}
