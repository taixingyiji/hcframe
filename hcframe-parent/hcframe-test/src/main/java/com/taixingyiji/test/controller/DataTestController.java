package com.taixingyiji.test.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.auth.entity.FtUser;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataTestController {

    final
    BaseMapper baseMapper;

    public DataTestController(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @GetMapping("save")
    public ResultVO<Integer> insertTest() {
        return ResultVO.getSuccess(baseMapper.save(FtUser.builder().username("test123").build()));
    }

}
