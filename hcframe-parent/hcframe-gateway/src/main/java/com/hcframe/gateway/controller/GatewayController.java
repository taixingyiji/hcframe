package com.hcframe.gateway.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.gateway_cas_stater.config.CasGatewayClientConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    final
    CasGatewayClientConfig casGatewayClientConfig;

    public GatewayController(CasGatewayClientConfig casGatewayClientConfig) {
        this.casGatewayClientConfig = casGatewayClientConfig;
    }

    @GetMapping("cas")
    public ResultVO<String> getCasUrl(){
        return ResultVO.getSuccess(casGatewayClientConfig.casServiceUrl+casGatewayClientConfig.casContextPath);
    }
}
