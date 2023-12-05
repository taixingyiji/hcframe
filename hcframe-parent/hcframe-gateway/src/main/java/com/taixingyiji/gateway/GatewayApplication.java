package com.taixingyiji.gateway;

import com.taixingyiji.gateway_cas_stater.annotate.EnableGatewayCas;
import com.taixingyiji.redis.RedisUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(RedisUtil.class)
@EnableGatewayCas
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
