package com.hcframe.config.module.cms;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.data.module.BaseMapper;
import com.hcframe.base.module.data.module.BaseMapperImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lhc
 * @version 1.0
 * @className CasConrtroller
 * @date 2021年04月12日 2:41 下午
 * @description 描述
 */
@RestController
@RequestMapping("/cms")
public class CasConrtroller {

    final BaseMapper baseMapper;

    public CasConrtroller(@Qualifier(BaseMapperImpl.BASE) BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    @GetMapping("")
    public ResultVO<Object> getCms() {
        return ResultVO.getSuccess();
    }
}
