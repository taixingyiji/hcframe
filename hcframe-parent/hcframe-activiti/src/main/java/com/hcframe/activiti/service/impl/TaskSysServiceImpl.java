package com.hcframe.activiti.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hcframe.activiti.activiti.ActivitiUtils;
import com.hcframe.activiti.activiti.CommonTask;
import com.hcframe.activiti.dao.BackReasonDao;
import com.hcframe.activiti.dao.CandidateUserDao;
import com.hcframe.activiti.dao.DataTaskDao;
import com.hcframe.activiti.dao.DataTaskHisDao;
import com.hcframe.activiti.entity.BackReason;
import com.hcframe.activiti.entity.CandidateUser;
import com.hcframe.activiti.entity.DataTask;
import com.hcframe.activiti.entity.DataTaskHis;
import com.hcframe.activiti.service.TaskSysService;
import com.hcframe.base.common.ResultVO;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.EmptyCheckUtils;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskSysServiceImpl implements TaskSysService {

    @Resource
    ActivitiUtils activitiUtils;

    @Resource
    private TaskService taskService;

    @Resource
    DataTaskDao dataTaskDao;

    @Resource
    DataTaskHisDao dataTaskHisDao;

    @Resource
    BackReasonDao backReasonDao;

    @Resource
    CandidateUserDao candidateUserDao;


    @Override
    public ResultVO getTaskList(CommonTask commonTask, WebPageInfo webPageInfo, Map<String, Object> map) {
        return ResultVO.getSuccess(activitiUtils.getTaskList(commonTask, webPageInfo, map));
    }

    @Override
    @Transactional
    public ResultVO startTaskByKey(String key, Map<String, Object> map) {
        EmptyCheckUtils.checkWithException(map, "参数列表不能为空");
        EmptyCheckUtils.checkWithException(map.get("tableName"), "tableName 不能为空!");
        EmptyCheckUtils.checkWithException(map.get("tableId"), "tableId不能为空！");
        Map<String, Object> activitiMap = activitiUtils.startProcessInstanceByKey(key, map);
        String processId = activitiMap.get("id").toString();
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println(task.getAssignee());
        String[] ids = map.get("tableId").toString().split(",");
        for (String id : ids) {
            DataTask dataTask = DataTask
                    .builder()
                    .taskId(task.getId())
                    .taskName(task.getName())
                    .taskKey(task.getTaskDefinitionKey())
                    .tableName(map.get("tableName").toString())
                    .tableId(id)
                    .processId(processId)
                    .assignee(task.getAssignee())
                    .build();
            int i = dataTaskDao.insertSelective(dataTask);
            if (i < 1) {
                throw new ServiceException("开始流程失败");
            }
        }
        activitiMap.put("taskId", task.getId());
        return ResultVO.getSuccess(activitiMap);
    }

    @Override
    public ResultVO startTaskById(String id, Map<String, Object> map) {
        EmptyCheckUtils.checkWithException(map, "参数列表不能为空");
        EmptyCheckUtils.checkWithException(map.get("tableName"), "tableName 不能为空!");
        EmptyCheckUtils.checkWithException(map.get("tableId"), "tableId不能为空！");
        return ResultVO.getSuccess(activitiUtils.startProcessInstanceById(id, map));
    }

    @Override
    @Transactional
    public ResultVO claimTask(String taskId, String userId) {
        Example example = new Example(DataTask.class);
        example.createCriteria().andEqualTo("taskId",taskId);
        dataTaskDao.updateByExampleSelective(DataTask.builder().assignee(userId).build(),example);
        return ResultVO.getSuccess(activitiUtils.claimTask(taskId, userId));
    }

    @Override
    @Transactional
    public ResultVO commitTask(String taskId, String map, Boolean isBack, String reason) {
        Map<String, Object> variables = JSONObject.parseObject(map);
        if (activitiUtils.completeTask(taskId, variables)) {
            DataTask dataTask = dataTaskDao.selectOne(DataTask.builder().taskId(taskId).build());
            DataTaskHis dataTaskHis = new DataTaskHis();
            BeanUtils.copyProperties(dataTask, dataTaskHis);
            dataTaskHis.setHisTaskId(null);
            int i = dataTaskHisDao.insertSelective(dataTaskHis);
            if (i < 1) {
                throw new ServiceException("任务提交失败");
            }
            List<DataTask> dataTaskList = dataTaskDao.select(DataTask.builder().processId(dataTask.getProcessId()).build());
            List<Integer> list = new ArrayList<>();
            for (DataTask data : dataTaskList) {
                list.add(data.getDataTaskId());
            }
            Example dataExample = new Example(DataTask.class);
            dataExample.createCriteria().andIn("dataTaskId", list);
            dataTaskDao.deleteByExample(dataExample);
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(dataTask.getProcessId()).list();
            for (Task task : taskList) {
                DataTask dataTask1 = DataTask
                        .builder()
                        .taskId(task.getId())
                        .taskName(task.getName())
                        .taskKey(task.getTaskDefinitionKey())
                        .tableName((String) taskService.getVariable(task.getId(), "tableName"))
                        .tableId((String) taskService.getVariable(task.getId(), "tableId"))
                        .processId(task.getProcessInstanceId())
                        .assignee(task.getAssignee())
                        .build();
                i = dataTaskDao.insertSelective(dataTask1);
                if (i < 1) {
                    throw new ServiceException("任务提交失败");
                }
            }
            return ResultVO.getSuccess();
        } else {
            return ResultVO.getFailed("完成任务失败");
        }
    }


    @Override
    @Transactional
    public ResultVO unclaimTask(String taskId) {
        Example example = new Example(DataTask.class);
        example.createCriteria().andEqualTo("taskId",taskId);
        dataTaskDao.updateByExampleSelective(DataTask.builder().assignee("0").build(),example);
        return ResultVO.getSuccess(activitiUtils.unClaimTask(taskId));
    }

    @Override
    @Transactional
    public ResultVO addCandidateUser(String taskId, String names) {
        for (String userId : names.split(",")) {
            candidateUserDao.insert(CandidateUser.builder().taskId(taskId).userId(userId).build());
        }
        return ResultVO.getSuccess(activitiUtils.addCandidateUser(taskId, names));
    }

    @Override
    public ResultVO getHighLightInfo(String taskId) {
        return null;
    }

    @Override
    @Transactional
    public ResultVO deleteTaskIstans(String instansId, String reason, boolean flag) {
        dataTaskDao.delete(DataTask.builder().processId(instansId).build());
        if (flag) {
            dataTaskHisDao.delete(DataTaskHis.builder().processId(instansId).build());
        }
        activitiUtils.deleteProcessInstance(instansId, reason, flag);
        return ResultVO.getSuccess();
    }

    @Override
    @Transactional
    public ResultVO deleteTaskIstansHitory(String instansId) {
        dataTaskHisDao.delete(DataTaskHis.builder().processId(instansId).build());
        return ResultVO.getSuccess(activitiUtils.deleteProcessInstanceHistory(instansId));
    }

    @Override
    public ResultVO getBackReason(String taskId) {
        BackReason backReason=backReasonDao.selectOne(BackReason.builder().taskId(taskId).build());
        return ResultVO.getSuccess(backReason);
    }
}
