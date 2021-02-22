package com.hcframe.base.module.datasource.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.datasource.annotation.DatasourceAnno;
import com.hcframe.base.module.datasource.service.DatasourceTypeService;
import com.hcframe.base.module.datasource.utils.DataUnit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhc
 * @date 2020-09-28
 * @description 数据库类型配置表
 */
@RestController
@RequestMapping("datatype")
public class DatasourceTypeController {

    @Autowired
    DatasourceTypeService datasourceTypeService;

    @GetMapping("all")
    @ApiOperation(value = "获取全部数据库类型信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getAllInfo() {
        return ResultVO.getSuccess(datasourceTypeService.getAllInfo());
    }
}
