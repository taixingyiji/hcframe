package com.taixingyiji.activiti.service;

import com.taixingyiji.activiti.activiti.CommonTask;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;

import java.util.Map;

public interface TaskSysService {

    ResultVO getTaskList(CommonTask commonTask, WebPageInfo webPageInfo, Map<String, Object> map);

    ResultVO startTaskByKey(String key, Map<String, Object> map);

    ResultVO startTaskById(String id, Map<String, Object> map);

    ResultVO claimTask(String taskId, String userId);

    ResultVO commitTask(String taskId,String map,Boolean isBack,String reason);

    ResultVO unclaimTask(String taskId);

    ResultVO addCandidateUser(String taskId, String names);

    ResultVO getHighLightInfo(String taskId);

    ResultVO deleteTaskIstans(String instansId, String reason, boolean flag);

    ResultVO deleteTaskIstansHitory(String instansId);

    ResultVO getBackReason(String taskId);
}
