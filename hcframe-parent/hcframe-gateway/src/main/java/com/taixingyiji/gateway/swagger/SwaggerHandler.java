package com.taixingyiji.gateway.swagger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

/**
 * @author lhc
 * @version 1.0
 * @className SwaggerHandler
 * @date 2021骞?4鏈?5鏃?5:01 涓嬪崍
 * @description 鎻忚堪
 */
@RestController
@RequestMapping("/swagger-resources")
public class SwaggerHandler {

    private final SwaggerProvider swaggerProvider;

    public SwaggerHandler(SwaggerProvider swaggerProvider) {
        this.swaggerProvider = swaggerProvider;
    }

    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<Map<String, Object>>> securityConfiguration() {
        return Mono.just(new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK));
    }

    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<Map<String, Object>>> uiConfiguration() {
        return Mono.just(new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK));
    }

    @GetMapping("")
    public Mono<ResponseEntity<Object>> swaggerResources() {
        return Mono.just(new ResponseEntity<>(swaggerProvider.get(), HttpStatus.OK));
    }
}
