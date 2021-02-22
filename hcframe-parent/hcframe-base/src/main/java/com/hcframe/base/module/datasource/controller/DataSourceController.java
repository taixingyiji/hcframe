package com.hcframe.base.module.datasource.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.datasource.annotation.DatasourceAnno;
import com.hcframe.base.module.datasource.entity.DatasourceConfig;
import com.hcframe.base.module.datasource.service.DataSourceService;
import com.hcframe.base.module.datasource.utils.DataUnit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhc
 * @date 2020-09-23
 */
@Api(tags = "数据源信息管理接口")
@RestController
@RequestMapping("/datasource")
public class DataSourceController {


    @Autowired
    DataSourceService dataSourceService;

    @GetMapping("hasSource")
    @ApiOperation(value = "判断是否配置数据源")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO isFirst() {
        return dataSourceService.hasSource();
    }

    @GetMapping("list")
    @ApiOperation(value = "获取数据源信息列表")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getDataSourceList(WebPageInfo webPageInfo, DatasourceConfig datasourceConfig) {
        return ResultVO.getSuccess(dataSourceService.list(webPageInfo, datasourceConfig));
    }

    @PostMapping("test")
    @ApiOperation(value = "测试数据库是否正常连接")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getTest(DatasourceConfig datasourceConfig) {
        return dataSourceService.test(datasourceConfig);
    }

    @PostMapping("")
    @ApiOperation(value = "新增数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO saveDatasource(DatasourceConfig datasourceConfig) {
        return dataSourceService.save(datasourceConfig);
    }

    @PutMapping("")
    @ApiOperation(value = "更新数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO update(DatasourceConfig datasourceConfig) {
        return dataSourceService.update(datasourceConfig);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO delete(@PathVariable Integer id) {
        return dataSourceService.delete(id);
    }

    @GetMapping("unique")
    @ApiOperation(value = "数据库Key或数据库名称是否唯一")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getUnique(String key, String name) {
        return dataSourceService.getUnique(key, name);
    }

    @PutMapping("enabled/{id}")
    @ApiOperation(value = "启用/禁用")
    @DatasourceAnno(DataUnit.SQLITE)
    @ApiImplicitParam(name = "status", value = "启用为1，禁用为0", required = true)
    public ResultVO enableDatasource(@PathVariable Integer id, @RequestParam Integer status) {
        return dataSourceService.enableDatasource(id, status);
    }

    @PutMapping("default/{id}")
    @ApiOperation(value = "设置默认数据库")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO setDefault(@PathVariable Integer id) {
        return dataSourceService.setDefault(id);
    }

    @GetMapping("all")
    @ApiOperation(value = "获取全部列表（启用）")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO getAllList() {
        return dataSourceService.getAllList();
    }

    @PostMapping("token")
    @ApiOperation(value = "token校验")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO validateToken(String token) {
        return dataSourceService.validateToken(token);
    }
}
