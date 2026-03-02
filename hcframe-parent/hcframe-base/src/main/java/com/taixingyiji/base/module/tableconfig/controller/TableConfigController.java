package com.taixingyiji.base.module.tableconfig.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.module.tableconfig.entity.OsSysField;
import com.taixingyiji.base.module.tableconfig.entity.OsSysSelect;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.taixingyiji.base.module.tableconfig.service.TableConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lhc
 * @date 2020-06-10
 * @description 单表配置修改接口
 */
@Tag(name = "单表配置修改接口")
@RestController
@RequestMapping("/tableConfig")
public class TableConfigController {

    @Autowired
    TableConfigService tableConfigService;

    @Operation(summary = "获取Table表信息")
    @GetMapping("/table")
    public ResultVO getTableInfo(OsSysTable osSysTable, WebPageInfo webPageInfo) {
        return tableConfigService.getTableInfo(osSysTable, webPageInfo);
    }

    @Operation(summary = "新增Table表信息")
    @PostMapping("/table")
    public ResultVO saveTableInfo(OsSysTable osSysTable) {
        return tableConfigService.saveTableInfo(osSysTable);
    }

    @Operation(summary = "批量删除表格信息")
    @DeleteMapping("table")
    public ResultVO deleteTable(String ids) {
        return tableConfigService.deleteTable(ids);
    }

    @Operation(summary = "编辑表格信息")
    @PutMapping("table")
    public ResultVO updateTable(OsSysTable osSysTable) {
        return tableConfigService.updateTable(osSysTable);
    }

    @Operation(summary = "获取字段信息")
    @GetMapping("field")
    public ResultVO getFieldList(OsSysField osSysField, WebPageInfo webPageInfo){
        return tableConfigService.getFieldList(osSysField, webPageInfo);
    }

    @Operation(summary = "新增字段信息")
    @PostMapping("field")
    public ResultVO saveFieldInfo(OsSysField osSysField){
        return tableConfigService.saveFieldInfo(osSysField);
    }

    @Operation(summary = "修改字段信息")
    @PutMapping("field")
    public ResultVO updateFieldInfo(OsSysField osSysField) {
        return tableConfigService.updateFieldInfo(osSysField);
    }

    @Operation(summary = "删除字段信息")
    @DeleteMapping("field")
    public ResultVO deleteField(String ids) {
        return tableConfigService.deleteField(ids);
    }

    @Operation(summary = "字段排序上移")
    @PutMapping("field/upMove")
    public ResultVO upMove(Integer id) {
        return tableConfigService.upMove(id);
    }

    @Operation(summary = "字段排序上移")
    @PutMapping("field/downMove")
    public ResultVO downMove(Integer id) {
        return tableConfigService.downMove(id);
    }

    @Operation(summary = "字段排序")
    @PutMapping("field/sort")
    public ResultVO fieldSort(Integer tableId) {
        return tableConfigService.fieldSort(tableId);
    }

    @Operation(summary = "获取下拉选项列表")
    @GetMapping("select")
    public ResultVO getSelectList(OsSysSelect osSysSelect) {
        return tableConfigService.getSelectList(osSysSelect);
    }

    @Operation(summary = "新增下拉列表")
    @PostMapping("select")
    public ResultVO saveSelectInfo(OsSysSelect osSysSelect) {
        return tableConfigService.saveSelectInfo(osSysSelect);
    }

    @Operation(summary = "更新下拉列表")
    @PutMapping("select")
    public ResultVO updateSelectInfo(OsSysSelect osSysSelect) {
        return tableConfigService.updateSelectInfo(osSysSelect);
    }

    @Operation(summary = "删除下拉列表")
    @DeleteMapping("select")
    public ResultVO delete(String ids) {
        return tableConfigService.deleteSelectInfo(ids);
    }

    @Operation(summary = "获取table下拉框")
    @GetMapping("tableSelect")
    public ResultVO getTableSelect(){
        return tableConfigService.getTableSelect();
    }
}
