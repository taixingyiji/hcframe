package com.taixingyiji.config.module.cloud;

import com.taixingyiji.base.common.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient("cloud-user")
public interface ManageService {

//    @GetMapping("/user/manage/{name}")
    ResultVO<String> getName(@PathVariable("name")String name);
}
