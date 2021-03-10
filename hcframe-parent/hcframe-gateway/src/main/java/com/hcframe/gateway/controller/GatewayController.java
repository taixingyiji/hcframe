package com.hcframe.gateway.controller;

import com.hcframe.base.common.ResultVO;
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
    public ResultVO<Map<String,String>> getCasUrl(){
        Map<String,String> map=new HashMap<>(2);
        map.put("cas", casGatewayClientConfig.casServiceUrl + casGatewayClientConfig.casContextPath + casGatewayClientConfig.getLoginUrl());
        map.put("config", "http://192.168.1.130:8084/user/cas/valid");
        return ResultVO.getSuccess(map);
    }
}
