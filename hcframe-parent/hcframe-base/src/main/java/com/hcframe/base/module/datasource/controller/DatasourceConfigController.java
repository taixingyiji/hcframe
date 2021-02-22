package com.hcframe.base.module.datasource.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.module.datasource.annotation.DatasourceAnno;
import com.hcframe.base.module.datasource.service.DatasourceConfigService;
import com.hcframe.base.module.datasource.utils.DataUnit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhc
 * @date 2020-09-23
 */
@Api(tags = "数据源配置管理接口")
@RestController
@RequestMapping("dataconfig")
public class DatasourceConfigController {

    @Autowired
    DatasourceConfigService datasourceConfigService;

    @PostMapping(value = "add")
    @DatasourceAnno(DataUnit.SQLITE)
    @ApiOperation(value = "新增")
    public ResultVO add(Integer id) {
        return datasourceConfigService.add(id);
    }

    @GetMapping("")
    @DatasourceAnno(DataUnit.SQLITE)
    @ApiOperation(value = "列表显示数据源状态")
    public ResultVO getRuntimeList() {
        return datasourceConfigService.getRuntimeList();
    }

    @DeleteMapping("/{key}")
    @DatasourceAnno(DataUnit.SQLITE)
    @ApiOperation(value = "终止数据源使用")
    public ResultVO deleteRuntimeSource(@PathVariable String key) {
        return datasourceConfigService.deleteRuntimeSource(key);
    }
}
