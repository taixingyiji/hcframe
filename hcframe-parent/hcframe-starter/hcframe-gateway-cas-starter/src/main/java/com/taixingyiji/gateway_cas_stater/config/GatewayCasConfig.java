package com.taixingyiji.gateway_cas_stater.config;

import com.taixingyiji.gateway_cas_stater.data.DataStorage;
import com.taixingyiji.gateway_cas_stater.filter.AuthGlobalFilter;
import com.taixingyiji.gateway_cas_stater.filter.CasValidGlobalFilter;
import com.taixingyiji.gateway_cas_stater.filter.SimpleCORSFilter;
import org.jasig.cas.client.util.ReflectUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lhc
 * @date 2021-03-02
 */
@Configuration
@EnableConfigurationProperties(CasGatewayClientConfig.class)
@AutoConfigureAfter(CasGatewayClientConfig.class)
public class GatewayCasConfig {

    private final CasGatewayClientConfig casGatewayClientConfig;

    public GatewayCasConfig(CasGatewayClientConfig casGatewayClientConfig) {
        this.casGatewayClientConfig = casGatewayClientConfig;
    }

    @Bean
    public DataStorage dataStorage() {
        Object[] args = new Long[1];
        args[0] = Long.valueOf(casGatewayClientConfig.millisBetweenCleanUps);
        return ReflectUtils.newInstance(casGatewayClientConfig.getCookieHolderPattern(), args);
    }

    @Bean
    public AuthGlobalFilter authGlobalFilter() {
        return new AuthGlobalFilter(casGatewayClientConfig, dataStorage());
    }

    @Bean
    public CasValidGlobalFilter casValidGlobalFilter() {
        return new CasValidGlobalFilter(casGatewayClientConfig, dataStorage());
    }

    @Bean
    public SimpleCORSFilter corsFilter(){
        return new SimpleCORSFilter();
    }

}
