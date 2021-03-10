package com.hcframe.gateway_cas_stater.controller;

import com.hcframe.gateway_cas_stater.config.CasGatewayClientConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayController {

    final
    CasGatewayClientConfig casGatewayClientConfig;

    public GatewayController(CasGatewayClientConfig casGatewayClientConfig) {
        this.casGatewayClientConfig = casGatewayClientConfig;
    }

    @GetMapping("gateway/cas")
    public Map<String,Object> getCasUrl(){
        Map<String, Object> resultMap = new HashMap<>(3);
        try {
            Map<String,String> map=new HashMap<>(2);
            map.put("cas", casGatewayClientConfig.casServiceUrl + casGatewayClientConfig.casContextPath + casGatewayClientConfig.getLoginUrl());
            map.put("config", casGatewayClientConfig.casServiceUrl);
            resultMap.put("code", 0);
            resultMap.put("msg", "");
            resultMap.put("data", map);
            return resultMap;
        } catch (Exception e) {
            resultMap.put("code", 2);
            resultMap.put("msg", e.getMessage());
            e.printStackTrace();
            return resultMap;
        }
    }
}
