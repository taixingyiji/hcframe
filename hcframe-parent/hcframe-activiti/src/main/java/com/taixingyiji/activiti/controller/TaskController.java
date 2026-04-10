package com.taixingyiji.activiti.controller;

import com.alibaba.fastjson.JSON;
import com.taixingyiji.activiti.activiti.CommonTask;
import com.taixingyiji.activiti.service.TaskSysService;
import com.taixingyiji.base.common.ResultVO;
import com.taixingyiji.base.common.WebPageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    TaskSysService taskSysService;

    @GetMapping("")
    public ResultVO getTaskList(CommonTask commonTask, WebPageInfo webPageInfo, String data) {
        Map<String, Object> map = JSON.parseObject(data);
        return taskSysService.getTaskList(commonTask, webPageInfo, map);
    }

    @PostMapping("/{key}/key")
    public ResultVO startTaskByKey(@PathVariable String key, @RequestParam Map<String,Object> map) {
        return taskSysService.startTaskByKey(key, map);
    }

    @PostMapping("/{id}/id")
    public ResultVO startTaskById(@PathVariable String id, Map<String, Object> map) {
        return taskSysService.startTaskById(id, map);
    }

    @PostMapping("/claim")
    public ResultVO claimTask(String taskId, String userId) {
        return taskSysService.claimTask(taskId, userId);
    }

    @PostMapping("/unclaim")
    public ResultVO unclaimTask(String taskId) {
        return taskSysService.unclaimTask(taskId);
    }

    @PostMapping("/{taskId}/commit")
    public ResultVO commitTask(@PathVariable String taskId, String map,Boolean isBack,String reason) {
        return taskSysService.commitTask(taskId, map,isBack,reason);
    }

    @PostMapping("/{taskId}/addCandidateUser")
    public ResultVO addCandidateUser(@PathVariable String taskId, String names) {
        return taskSysService.addCandidateUser(taskId, names);
    }

    @PostMapping("/{taskId}/highLight")
    public ResultVO getHighLightInfo(@PathVariable String taskId) {
        return taskSysService.getHighLightInfo(taskId);
    }

    @DeleteMapping("/{instansId}")
    public ResultVO deleteTaskInstans(@PathVariable String instansId,@RequestParam String reason,@RequestParam Boolean flag){
        return taskSysService.deleteTaskIstans(instansId, reason,flag);
    }

    @DeleteMapping("/{instansId}/history")
    public ResultVO deleteTaskHisInstans(@PathVariable String instansId){
        return taskSysService.deleteTaskIstansHitory(instansId);
    }

    @GetMapping("/{taskId}/reason")
    public ResultVO getBackReason(@PathVariable String taskId) {
        return taskSysService.getBackReason(taskId);
    }


}
