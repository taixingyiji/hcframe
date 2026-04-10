package com.taixingyiji.activiti.controller;

import com.taixingyiji.activiti.activiti.CommonProcessDefinition;
import com.taixingyiji.activiti.service.ProcessService;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author lhc
 */
@RequestMapping("/process")
@RestController
public class ProcessController {

    @Resource
    ProcessService processService;

    @GetMapping("")
    public ResultVO getProcessDefinitionList(CommonProcessDefinition processDefinition, WebPageInfo webPageInfo,
                                             @RequestParam(defaultValue = "true") Boolean isLatestVersion) {
        return processService.getProcessDefinitionList(processDefinition, webPageInfo, isLatestVersion);
    }

    @DeleteMapping("/{id}")
    public ResultVO deleteProcessDefinition(@PathVariable String id, boolean state) {
        return processService.deleteProcessDefinitionList(id, state);
    }

    @PostMapping("")
    public ResultVO addProcessDefinition(@Param("file") MultipartFile file, String name) {
        return processService.addProccessDefinition(file, name);
    }

    @PostMapping("/byXml")
    public ResultVO addProcessDefinitionByXMl(String file, String name,String fileName) {
        return processService.addProccessDefinitionByXml(file, name,fileName);
    }

    @PutMapping("/byXml")
    public ResultVO updateProcessDefinitionByXml(String file, String name, String fileName,Integer version) {
        return processService.updateProcessDefinitionByXml(file, name, fileName,version);
    }

    @GetMapping("bpmn")
    public ResultVO getBpmnFile(String deploymentId,String resourceName, HttpServletResponse response) {
        return processService.getBpmnFile(deploymentId,resourceName, response);
    }

    @PostMapping("unique")
    public ResultVO uniqueRecord(String name, String key) {
        return processService.uniqueRecord(name, key);
    }

}
