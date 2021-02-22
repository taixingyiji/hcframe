package com.hcframe.activiti.service;

import com.hcframe.activiti.activiti.CommonProcessDefinition;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ProcessService {

    ResultVO getProcessDefinitionList(CommonProcessDefinition processDefinition, WebPageInfo webPageInfo, Boolean isLatestVersion);

    ResultVO deleteProcessDefinitionList(String id, boolean state);

    ResultVO addProccessDefinition(MultipartFile file, String name);

    ResultVO getBpmnFile(String deploymentId, String resourceName, HttpServletResponse response);

    ResultVO addProccessDefinitionByXml(String file, String name,String fileName);

    ResultVO updateProcessDefinitionByXml(String file, String name, String fileName, Integer version);

    ResultVO uniqueRecord(String name, String key);
}
