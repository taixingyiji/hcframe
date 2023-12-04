package com.taixingyiji.base.module.data.controller;

import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import com.taixingyiji.base.common.utils.TableNameUtil;
import com.taixingyiji.base.module.data.service.TableService;
import com.taixingyiji.base.module.tableconfig.entity.OsSysTable;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/***
 * @description 控制器提供接口
 */
@Api(tags = "通用增删改查接口")
@RequestMapping("common")
@RestController// 控制器注解，告诉Spring框架进行管理，并返回Json格式字符串，将@ResponeseBody 和 @Controller注解功能合并的注解
public class TableController {

    final
    TableService tableService;

    final
    TableNameUtil tableNameUtil;

    public TableController(TableService tableService, TableNameUtil tableNameUtil) {
        this.tableService = tableService;
        this.tableNameUtil = tableNameUtil;
    }

    @ApiOperation(value = "保存接口（不带保存日期）")
    @PostMapping("/{typeName}")
    public ResultVO<Integer> save(@PathVariable String typeName, @RequestParam Map<String, Object> map) {
        return ResultVO.getSuccess(tableService.save(tableNameUtil.getTableName(typeName), map));
    }

    @ApiOperation(value = "保存接口（带保存日期）")
    @PostMapping("/{typeName}/date")
    public ResultVO<Map<String,Object>> saveWithDate(@PathVariable String typeName, @RequestParam Map<String, Object> map) {
        return tableService.saveWithDate(tableNameUtil.getTableName(typeName), map);
    }

    @ApiOperation(value = "更新接口")
    @PutMapping({"/{typeName}/{version}","/{typeName}"})
    public ResultVO<Integer> update(@PathVariable String typeName, @RequestParam Map<String, Object> map,@PathVariable(required = false) Integer version) {
        return tableService.update(tableNameUtil.getTableName(typeName), map,version);
    }

    @ApiOperation(value = "更新接口(带更改日期)")
    @PutMapping({"/{typeName}/{version}/date","/{typeName}/date"})
    public ResultVO<Integer> updateWithDate(@PathVariable String typeName, @RequestParam Map<String, Object> map,@PathVariable(required = false) Integer version) {
        return tableService.updateWithDate(tableNameUtil.getTableName(typeName), map,version);
    }

    @ApiOperation(value = "删除接口（可批量）")
    @DeleteMapping("/{typeName}")
    @ApiImplicitParam(name = "ids", value = "id主键的数组的toString", required = true)
    public ResultVO<Integer> delete(@PathVariable String typeName, String ids) {
        return tableService.delete(tableNameUtil.getTableName(typeName), ids);
    }


    @ApiOperation(value = "逻辑删除（可批量）")
    @DeleteMapping("/{typeName}/logic")
    @ApiImplicitParam(name = "ids", value = "id主键的数组的toString", required = true)
    public ResultVO<Integer> logicDelete(@PathVariable String typeName,String ids) {
        return tableService.logicDelete(tableNameUtil.getTableName(typeName), ids);
    }

    @ApiOperation(value = "获取单表数据接口（带分页）")
    @GetMapping("/{typeName}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeName", value = " 类型", type = "path", required = true),
            @ApiImplicitParam(name = "data", value = " JSON.stringify()后的数据，主要为查询条件"),
    })
    public ResultVO<PageInfo<Map<String,Object>>> searchTables(@PathVariable String typeName, String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchSingleTables(data, tableNameUtil.getTableName(typeName), webPageInfo));
    }

    @ApiOperation(value = "获取连表数据接口（带分页）")
    @GetMapping("/{typeName}/join")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeName", value = " 类型", type = "path", required = true),
            @ApiImplicitParam(name = "data", value = " JSON.stringify()后的数据，主要为查询条件"),
    })
    public ResultVO<PageInfo<Map<String,Object>>> searchJoinTables(@PathVariable String typeName, String data, WebPageInfo webPageInfo) {
        return ResultVO.getSuccess(tableService.searchJoinTables(data, webPageInfo,tableNameUtil.getTableName(typeName)));
    }

    @ApiOperation(value = "获取单表接口（不带分页）")
    @GetMapping("/getListNoPage/{typeName}")
    public ResultVO<List<Map<String,Object>>> getListNoPage(@PathVariable String typeName, @RequestParam Map<String, Object> map) {
        return tableService.getListNoPage(tableNameUtil.getTableName(typeName), map);
    }

    @ApiOperation(value = "获取表格全部信息")
    @GetMapping("/{typeName}/getTableInfo/")
    public ResultVO<OsSysTable> getTableInfo(@PathVariable String typeName) {
        return ResultVO.getSuccess(tableNameUtil.getTableAllInfo(typeName));
    }

    @ApiOperation(value = "批量更新")
    @PutMapping("/{typeName}/batch")
    public ResultVO<Integer> updateBatch(@PathVariable String typeName, @RequestParam Map<String, Object> map) {
        return tableService.updateBatch(tableNameUtil.getTableName(typeName), map);
    }

    @ApiOperation(value = "批量更新(带日期)")
    @PutMapping("/{typeName}/batchWithDate")
    public ResultVO<Integer> updateBatchWithDate(@PathVariable String typeName, @RequestParam Map<String, Object> map) {
        return tableService.updateBatchWithDate(tableNameUtil.getTableName(typeName), map);
    }

    @ApiOperation(value = "批量新增")
    @PostMapping("/{typeName}/batch")
    public ResultVO<Integer> saveBatch(@PathVariable String typeName, @RequestParam String data) {
        return tableService.saveBatch(tableNameUtil.getTableName(typeName), data);
    }

    @ApiOperation(value = "批量新增(带日期)")
    @PostMapping("/{typeName}/batchWithDate")
    public ResultVO<Integer> saveBatchWithDate(@PathVariable String typeName, @RequestParam String data) {
        return tableService.saveBatchWithDate(tableNameUtil.getTableName(typeName), data);
    }

    @ApiOperation(value = "获取基表信息")
    @GetMapping("/getBaseTableInfo")
    public ResultVO<Map<String,Object>> getBaseTableInfo(String tableNames) {
        return tableService.getBaseTableInfo(tableNames);
    }

}
