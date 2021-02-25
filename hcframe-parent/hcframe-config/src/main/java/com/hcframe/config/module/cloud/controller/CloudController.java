package com.hcframe.config.module.cloud.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.config.module.cloud.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudController {

    final
    ManageService manageService;

    public CloudController(ManageService manageService) {
        this.manageService = manageService;
    }

    @GetMapping("/{name}")
    public ResultVO<String> getName(@PathVariable String name){
        return manageService.getName(name);
    }
}
