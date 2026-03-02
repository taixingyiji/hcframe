package com.taixingyiji.base.module.tableconfig.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.datasource.entity.DatasourceConfig;
import com.taixingyiji.base.module.tableconfig.entity.FieldInfo;
import com.taixingyiji.base.module.tableconfig.entity.TableInfo;
import com.taixingyiji.base.module.tableconfig.service.TableGenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lhc
 * @date 2020-06-10
 * @description 自动填充生成表单数据接口
 */
@Tag(name = "自动填充表单数据接口")
@RestController
@RequestMapping("/tableGen")
public class TableGenController {

    @Autowired
    TableGenService tableGenService;

    @Operation(summary = "自动生成序列")
    @PostMapping("genAllSequence")
    public ResultVO genAllSequence() {
        return tableGenService.genAllSequence();
    }

    @Operation(summary = "获取table表接口")
    @GetMapping("list")
    public ResultVO getTableList(WebPageInfo webPageInfo, TableInfo tableInfo, DatasourceConfig datasourceConfig) {
        return tableGenService.getTableList(webPageInfo, tableInfo, datasourceConfig);
    }

    @Operation(summary = "获取字段接口")
    @GetMapping("fieldList")
    public ResultVO getFieldList(WebPageInfo webPageInfo, FieldInfo fieldInfo, DatasourceConfig datasourceConfig, String tableName) {
        return tableGenService.getFieldList(webPageInfo, fieldInfo,datasourceConfig,tableName);
    }

    @Operation(summary = "自动填充Table表信息")
    @PostMapping("genTable")
    public ResultVO genTable( DatasourceConfig datasourceConfig) {
        return tableGenService.genTable(datasourceConfig);
    }

    @Operation(summary = "自动填充Field表信息")
    @PostMapping("genField")
    public ResultVO genField(DatasourceConfig datasourceConfig,String tableAlias) {
        return tableGenService.genField(datasourceConfig,tableAlias);
    }

    @Operation(summary = "自动生成全部信息")
    @PostMapping("genAll")
    public ResultVO genAll(DatasourceConfig datasourceConfig) {
        return tableGenService.genAll(datasourceConfig);
    }
}
