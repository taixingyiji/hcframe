package com.hcframe.base.module.tableconfig.controller;

import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.module.tableconfig.entity.OsSysField;
import com.hcframe.base.module.tableconfig.entity.OsSysSelect;
import com.hcframe.base.module.tableconfig.entity.OsSysTable;
import com.hcframe.base.module.tableconfig.service.TableConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhc
 * @date 2020-06-10
 * @description 单表配置修改接口
 */
@Api(tags = "单表配置修改接口")
@RestController
@RequestMapping("/tableConfig")
public class TableConfigController {

    @Autowired
    TableConfigService tableConfigService;

    @ApiOperation(value = "获取Table表信息")
    @GetMapping("/table")
    public ResultVO getTableInfo(OsSysTable osSysTable, WebPageInfo webPageInfo) {
        return tableConfigService.getTableInfo(osSysTable, webPageInfo);
    }

    @ApiOperation(value = "新增Table表信息")
    @PostMapping("/table")
    public ResultVO saveTableInfo(OsSysTable osSysTable) {
        return tableConfigService.saveTableInfo(osSysTable);
    }

    @ApiOperation(value = "批量删除表格信息")
    @DeleteMapping("table")
    public ResultVO deleteTable(String ids) {
        return tableConfigService.deleteTable(ids);
    }

    @ApiOperation(value = "编辑表格信息")
    @PutMapping("table")
    public ResultVO updateTable(OsSysTable osSysTable) {
        return tableConfigService.updateTable(osSysTable);
    }

    @ApiOperation(value = "获取字段信息")
    @GetMapping("field")
    public ResultVO getFieldList(OsSysField osSysField, WebPageInfo webPageInfo){
        return tableConfigService.getFieldList(osSysField, webPageInfo);
    }

    @ApiOperation(value = "新增字段信息")
    @PostMapping("field")
    public ResultVO saveFieldInfo(OsSysField osSysField){
        return tableConfigService.saveFieldInfo(osSysField);
    }

    @ApiOperation(value = "修改字段信息")
    @PutMapping("field")
    public ResultVO updateFieldInfo(OsSysField osSysField) {
        return tableConfigService.updateFieldInfo(osSysField);
    }

    @ApiOperation(value = "删除字段信息")
    @DeleteMapping("field")
    public ResultVO deleteField(String ids) {
        return tableConfigService.deleteField(ids);
    }

    @ApiOperation(value = "获取下拉选项列表")
    @GetMapping("select")
    public ResultVO getSelectList(OsSysSelect osSysSelect) {
        return tableConfigService.getSelectList(osSysSelect);
    }

    @ApiOperation(value = "新增下拉列表")
    @PostMapping("select")
    public ResultVO saveSelectInfo(OsSysSelect osSysSelect) {
        return tableConfigService.saveSelectInfo(osSysSelect);
    }

    @ApiOperation(value = "更新下拉列表")
    @PutMapping("select")
    public ResultVO updateSelectInfo(OsSysSelect osSysSelect) {
        return tableConfigService.updateSelectInfo(osSysSelect);
    }

    @ApiOperation(value = "删除下拉列表")
    @DeleteMapping("select")
    public ResultVO delete(String ids) {
        return tableConfigService.deleteSelectInfo(ids);
    }

    @ApiOperation(value = "获取table下拉框")
    @GetMapping("tableSelect")
    public ResultVO getTableSelect(){
        return tableConfigService.getTableSelect();
    }
}
