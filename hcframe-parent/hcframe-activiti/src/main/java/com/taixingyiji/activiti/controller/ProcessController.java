package com.taixingyiji.activiti.controller;

import com.taixingyiji.activiti.activiti.CommonProcessDefinition;
import com.taixingyiji.activiti.service.ProcessService;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lhc
 */
@Api(tags = "activiti操作接口")
@RequestMapping("/process")
@RestController
public class ProcessController {

    @Resource
    ProcessService processService;

    @ApiOperation(value = "获取工作流Process列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isLatestVersion", value = "是否只显示最新版", allowableValues = "true,false", defaultValue = "false")
    })
    @GetMapping("")
    public ResultVO getProcessDefinitionList(CommonProcessDefinition processDefinition, WebPageInfo webPageInfo,
                                             @RequestParam(defaultValue = "true") Boolean isLatestVersion) {
        return processService.getProcessDefinitionList(processDefinition, webPageInfo, isLatestVersion);
    }

    @ApiOperation(value = "删除工作流列表Process列表")
    @DeleteMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "状态，为true则删除全部关联数据，为false则只删除Process", required = true),
            @ApiImplicitParam(name = "id", value = "获取流程的deploymentId", required = true)
    })
    public ResultVO deleteProcessDefinition(@PathVariable String id, boolean state) {
        return processService.deleteProcessDefinitionList(id, state);
    }

    @ApiOperation(value = "新增/更新Process，若为更新只需文件名和process名称保持一致即可")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程名称"),
            @ApiImplicitParam(name = "file", value = "上传的文件")
    })
    @PostMapping("")
    public ResultVO addProcessDefinition(@Param("file") MultipartFile file, String name) {
        return processService.addProccessDefinition(file, name);
    }

    @ApiOperation(value = "新增Process，通过xml字符串方式更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程名称",required = true),
            @ApiImplicitParam(name = "file", value = "上传的文件",required = true),
            @ApiImplicitParam(name = "fileName", value = "文件名称需要有后缀.bpmn",required = true)
    })
    @PostMapping("/byXml")
    public ResultVO addProcessDefinitionByXMl(String file, String name,String fileName) {
        return processService.addProccessDefinitionByXml(file, name,fileName);
    }

    @ApiOperation(value = "修改Process，通过xml字符串方式更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "流程名称",required = true),
            @ApiImplicitParam(name = "file", value = "上传的文件",required = true),
            @ApiImplicitParam(name = "fileName", value = "文件名称需要有后缀.bpmn",required = true),
            @ApiImplicitParam(name = "version", value = "当前版本(数字)",required = true)
    })
    @PutMapping("/byXml")
    public ResultVO updateProcessDefinitionByXml(String file, String name, String fileName,Integer version) {
        return processService.updateProcessDefinitionByXml(file, name, fileName,version);
    }

    @ApiOperation(value = "获取流程图bpmn流程图并展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deploymentId", value = "流程发布Id"),
            @ApiImplicitParam(name = "resourceName", value = "流程发布Id")
    })
    @GetMapping("bpmn")
    public ResultVO getBpmnFile(String deploymentId,String resourceName, HttpServletResponse response) {
        return processService.getBpmnFile(deploymentId,resourceName, response);
    }

    @ApiOperation(value = "校验key和name是否唯一")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true),
            @ApiImplicitParam(name = "key", value = "标识", required = true)
    })
    @PostMapping("unique")
    public ResultVO uniqueRecord(String name, String key) {
        return processService.uniqueRecord(name, key);
    }

}
