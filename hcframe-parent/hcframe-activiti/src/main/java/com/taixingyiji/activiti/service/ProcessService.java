package com.taixingyiji.activiti.service;

import com.taixingyiji.activiti.activiti.CommonProcessDefinition;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
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
