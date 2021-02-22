package com.hcframe.activiti.controller;

import com.alibaba.fastjson.JSON;
import com.hcframe.activiti.activiti.CommonTask;
import com.hcframe.activiti.service.TaskSysService;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.WebPageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "获取工作状态")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Resource
    TaskSysService taskSysService;

    @ApiOperation(value = "获取任务列表")
    @GetMapping("")
    public ResultVO getTaskList(CommonTask commonTask, WebPageInfo webPageInfo, String data) {
        Map<String, Object> map = JSON.parseObject(data);
        return taskSysService.getTaskList(commonTask, webPageInfo, map);
    }

    @ApiOperation(value = "开始一个流程，设置流程人员参数等（通过key）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名，与流程绑定", required = true),
            @ApiImplicitParam(name = "tableId", value = "表Id,与流程绑定", required = true),
            @ApiImplicitParam(name = "key", value = "ProcessInstance的key值", required = true),
            @ApiImplicitParam(name = "...", value = "其余参数请根据需求，及设计好的流程图进行设计", required = true)
    })
    @PostMapping("/{key}/key")
    public ResultVO startTaskByKey(@PathVariable String key, @RequestParam Map<String,Object> map) {
        return taskSysService.startTaskByKey(key, map);
    }

    @ApiOperation(value = "开始一个流程，设置人员参数等（通过Id）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名，与流程绑定", required = true),
            @ApiImplicitParam(name = "tableId", value = "表Id,与流程绑定", required = true),
            @ApiImplicitParam(name = "id", value = "ProcessInstance的Id", required = true),
            @ApiImplicitParam(name = "...", value = "其余参数请根据需求，及设计好的流程图进行设计", required = true)
    })
    @PostMapping("/{id}/id")
    public ResultVO startTaskById(@PathVariable String id, Map<String, Object> map) {
        return taskSysService.startTaskById(id, map);
    }

    @ApiOperation(value = "流程记录加锁")
    @PostMapping("/claim")
    public ResultVO claimTask(String taskId, String userId) {
        return taskSysService.claimTask(taskId, userId);
    }

    @ApiOperation(value = "任务解锁")
    @PostMapping("/unclaim")
    public ResultVO unclaimTask(String taskId) {
        return taskSysService.unclaimTask(taskId);
    }

    @ApiOperation(value = "完成任务")
    @PostMapping("/{taskId}/commit")
    public ResultVO commitTask(@PathVariable String taskId, String map,Boolean isBack,String reason) {
        return taskSysService.commitTask(taskId, map,isBack,reason);
    }

    @ApiOperation(value = "设置候选人（可设置多个候选人，标识符逗号隔开即可）")
    @PostMapping("/{taskId}/addCandidateUser")
    public ResultVO addCandidateUser(@PathVariable String taskId, String names) {
        return taskSysService.addCandidateUser(taskId, names);
    }

    @ApiOperation(value = "获取任务高亮json")
    @PostMapping("/{taskId}/highLight")
    public ResultVO getHighLightInfo(@PathVariable String taskId) {
        return taskSysService.getHighLightInfo(taskId);
    }

    @ApiOperation(value = "删除一个进行中流程实例")
    @DeleteMapping("/{instansId}")
    public ResultVO deleteTaskInstans(@PathVariable String instansId,@RequestParam String reason,@RequestParam Boolean flag){
        return taskSysService.deleteTaskIstans(instansId, reason,flag);
    }

    @ApiOperation(value = "删除一个历史流程实例")
    @DeleteMapping("/{instansId}/history")
    public ResultVO deleteTaskHisInstans(@PathVariable String instansId){
        return taskSysService.deleteTaskIstansHitory(instansId);
    }

    @ApiOperation(value = "获取退回意见")
    @GetMapping("/{taskId}/reason")
    public ResultVO getBackReason(@PathVariable String taskId) {
        return taskSysService.getBackReason(taskId);
    }


}
