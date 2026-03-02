package com.taixingyiji.base.module.datasource.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.datasource.annotation.DatasourceAnno;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.datasource.service.DataSourceService;
import com.taixingyiji.base.module.datasource.utils.DataUnit;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhc
 * @date 2020-09-23
 */
@Tag(name = "数据源信息管理接口")
@RestController
@RequestMapping("/datasource")
public class DataSourceController {


    @Autowired
    DataSourceService dataSourceService;

    @GetMapping("hasSource")
    @Operation(summary = "判断是否配置数据源")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO isFirst() {
        return dataSourceService.hasSource();
    }

    @GetMapping("list")
    @Operation(summary = "获取数据源信息列表")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getDataSourceList(WebPageInfo webPageInfo, DatasourceConfig datasourceConfig) {
        return ResultVO.getSuccess(dataSourceService.list(webPageInfo, datasourceConfig));
    }

    @PostMapping("test")
    @Operation(summary = "测试数据库是否正常连接")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getTest(DatasourceConfig datasourceConfig) {
        return dataSourceService.test(datasourceConfig);
    }

    @PostMapping("")
    @Operation(summary = "新增数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO saveDatasource(DatasourceConfig datasourceConfig) {
        return dataSourceService.save(datasourceConfig);
    }

    @PutMapping("")
    @Operation(summary = "更新数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO update(DatasourceConfig datasourceConfig) {
        return dataSourceService.update(datasourceConfig);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数据库信息")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO delete(@PathVariable Integer id) {
        return dataSourceService.delete(id);
    }

    @GetMapping("unique")
    @Operation(summary = "数据库Key或数据库名称是否唯一")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO getUnique(String key, String name) {
        return dataSourceService.getUnique(key, name);
    }

    @PutMapping("enabled/{id}")
    @Operation(summary = "启用/禁用")
    @DatasourceAnno(DataUnit.SQLITE)
    public ResultVO enableDatasource(@PathVariable Integer id, @Parameter(description = "启用为1，禁用为0", required = true) @RequestParam Integer status) {
        return dataSourceService.enableDatasource(id, status);
    }

    @PutMapping("default/{id}")
    @Operation(summary = "设置默认数据库")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO setDefault(@PathVariable Integer id) {
        return dataSourceService.setDefault(id);
    }

    @GetMapping("all")
    @Operation(summary = "获取全部列表（启用）")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO getAllList() {
        return dataSourceService.getAllList();
    }

    @PostMapping("token")
    @Operation(summary = "token校验")
    @DatasourceAnno(value = DataUnit.SQLITE)
    public ResultVO validateToken(String token) {
        return dataSourceService.validateToken(token);
    }
}
