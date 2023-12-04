package com.taixingyiji.activiti.activiti;

import com.hcframe.base.common.ResultPageInfo;
import com.hcframe.base.common.ServiceException;
import com.hcframe.base.common.WebPageInfo;
import com.hcframe.base.common.utils.EmptyCheckUtils;
import org.activiti.engine.*;
import org.activiti.engine.impl.ProcessInstanceQueryProperty;
import org.activiti.engine.impl.TaskQueryProperty;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author lhc
 * @date 2020-07-06
 * @description activiti工具类
 */
@Component
public class ActivitiUtils {

    private final static Logger logger = LoggerFactory.getLogger(ActivitiUtils.class);

    @Resource
    RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private ManagementService managementService;


    /**
     * 发布流程classpath
     *
     * @param name      流程名称
     * @param classpath 流程文件地址,在项目classpath路径下的相对路径地址
     * @return 流程信息
     */
    public Deployment deployProcessFormClasspath(String name, String classpath) {
        EmptyCheckUtils.checkWithException(name, "name不能为空！");
        EmptyCheckUtils.checkWithException(classpath, "classpath不能为空");
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource(classpath)
                    .name(name)
                    .deploy();
            logger.info("流程ID：" + deployment.getId());
            logger.info("流程名称：" + deployment.getName());
            return deployment;
        } catch (Exception e) {
            logger.error("流程发布失败", e);
            throw new ServiceException("流程发布失败！");
        }
    }

    /**
     * 发布流程基于InputStream
     *
     * @param name 流程名称
     * @param is   输入流
     * @return 流程信息
     */
    public Deployment deployProcessFormInputStream(String name, InputStream is, String fileName) {
        EmptyCheckUtils.checkWithException(name, "name不能为空！");
        EmptyCheckUtils.checkWithException(fileName, "fileName不能为空！");
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(fileName, is)
                    .name(name)
                    .deploy();
            logger.info("流程ID：" + deployment.getId());
            logger.info("流程名称：" + deployment.getName());
            return deployment;
        } catch (Exception e) {
            logger.error("流程发布失败", e);
            throw new ServiceException("流程发布失败！");
        }
    }

    /**
     * 删除一个process实例
     *
     * @param id   processId
     * @param flag 是否连级删除所有信息
     * @return
     */
    public boolean deleteDeploymenet(String id, boolean flag) {
        EmptyCheckUtils.checkWithException(id, "id不能为空！");
        try {
            repositoryService.deleteDeployment(id, flag);
            return true;
        } catch (Exception e) {
            logger.error("删除失败", e);
            throw new ServiceException("删除失败");
        }
    }

    /**
     * 获取流程列表
     *
     * @param commonProcessDefinition 查询参数，可查询内容包括name,key,version
     * @param webPageInfo             分页及排序参数
     * @param isLatestVersion         是否只显示最新版本
     * @return 返回结果列表
     */
    public ResultPageInfo<CommonProcessDefinition> getDeployProcessList(CommonProcessDefinition commonProcessDefinition,
                                                                        WebPageInfo webPageInfo, Boolean isLatestVersion) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        if (WebPageInfo.hasSort(webPageInfo)) {
            ProcessInstanceQueryProperty queryProperty = new ProcessInstanceQueryProperty(webPageInfo.getSortField());
            query = query.orderBy(queryProperty);
            if (webPageInfo.getOrder().equals(WebPageInfo.ASC)) {
                query = query.asc();
            } else {
                query = query.desc();
            }
        }
        if (StringUtils.isNotEmpty(commonProcessDefinition.getName())) {
            query = query.processDefinitionNameLike("%" + commonProcessDefinition.getName() + "%");
        }
        if (StringUtils.isNotEmpty(commonProcessDefinition.getKey())) {
            query = query.processDefinitionNameLike("%" + commonProcessDefinition.getKey() + "%");
        }
        if (!org.springframework.util.StringUtils.isEmpty(commonProcessDefinition.getVersion())) {
            query = query.processDefinitionVersion(commonProcessDefinition.getVersion());
        }
        if (isLatestVersion) {
            query = query.latestVersion();
        }
        long total = query.count();
        List<ProcessDefinition> list = query.listPage((webPageInfo.getPageNum() - 1) * webPageInfo.getPageSize(), webPageInfo.getPageSize());
        List<CommonProcessDefinition> resultList = new LinkedList<>();
        for (ProcessDefinition processDefinition : list) {
            CommonProcessDefinition commonProcessDefinition1 = new CommonProcessDefinition();
            BeanUtils.copyProperties(processDefinition, commonProcessDefinition1);
            resultList.add(commonProcessDefinition1);
        }
        ResultPageInfo<CommonProcessDefinition> resultPageInfo = new ResultPageInfo<>();
        resultPageInfo.setList(resultList);
        resultPageInfo.setTotal(total);
        return resultPageInfo;
    }

    /**
     * 获取单个发布流程实例
     *
     * @param deploymentId 发布实例
     * @return 返回单个实例结果
     */
    public CommonProcessDefinition getSingleProcessById(String deploymentId) {
        EmptyCheckUtils.checkWithException(deploymentId, "deploymentId不能为空！");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        CommonProcessDefinition commonProcessDefinition = new CommonProcessDefinition();
        BeanUtils.copyProperties(processDefinition, commonProcessDefinition);
        return commonProcessDefinition;
    }

    /**
     * 通过Key开始一个流程
     *
     * @param key 流程key
     * @param map 流程变量
     * @return 流程实例
     */
    public Map<String, Object> startProcessInstanceByKey(String key, Map<String, Object> map) {
        EmptyCheckUtils.checkWithException(key, "key不能为空！");
        try {
            ProcessInstance instance;
            if (map == null || map.isEmpty()) {
                instance = runtimeService.startProcessInstanceByKey(key);
            } else {
                instance = runtimeService.startProcessInstanceByKey(key, map);
            }
            logger.info("流程实例ID:" + instance.getId());
            logger.info("流程定义ID:" + instance.getProcessDefinitionId());
            logger.info("流程发布ID:" + instance.getDeploymentId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", instance.getId());
            resultMap.put("processDefinitionId", instance.getProcessDefinitionId());
            return resultMap;
        } catch (Exception e) {
            logger.error("流程开始失败：", e);
            throw new ServiceException("流程开始失败！");
        }
    }


    /**
     * 通过Id开始一个流程
     *
     * @param id  流程Id
     * @param map 流程变量
     * @return 流程实例
     */
    public Map<String, Object> startProcessInstanceById(String id, Map<String, Object> map) {
        EmptyCheckUtils.checkWithException(id, "id不能为空！");
        try {
            ProcessInstance instance;
            if (map == null || map.isEmpty()) {
                instance = runtimeService.startProcessInstanceById(id);
            } else {
                instance = runtimeService.startProcessInstanceByKey(id, map);
            }

            logger.info("流程实例ID:" + instance.getId());
            logger.info("流程定义ID:" + instance.getProcessDefinitionId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", instance.getId());
            resultMap.put("deploymentId", instance.getDeploymentId());
            resultMap.put("processDefinitionId", instance.getProcessDefinitionId());
            return resultMap;
        } catch (Exception e) {
            logger.error("流程开始失败：", e);
            throw new ServiceException("流程开始失败！");
        }
    }

    /**
     * 执行任务
     *
     * @param taskId 任务Id
     * @return 是否成功
     */
    public boolean completeTask(String taskId) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        try {
            taskService.complete(taskId);
            logger.info("完成任务：任务ID：" + taskId);
            return true;
        } catch (Exception e) {
            logger.error("任务执行失败", e);
            throw new ServiceException("任务执行失败！");
        }
    }

    /**
     * 获取任务列表
     *
     * @param commonTask
     * @param webPageInfo
     * @param map
     * @return
     */
    public ResultPageInfo<CommonTask> getTaskList(CommonTask commonTask, WebPageInfo webPageInfo, Map<String, Object> map) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (WebPageInfo.hasSort(webPageInfo)) {
            TaskQueryProperty queryProperty = new TaskQueryProperty(webPageInfo.getSortField());
            taskQuery = taskQuery.orderBy(queryProperty);
            if (webPageInfo.getOrder().equals(WebPageInfo.ASC)) {
                taskQuery = taskQuery.asc();
            } else {
                taskQuery = taskQuery.desc();
            }
        }
        if (StringUtils.isNotEmpty(commonTask.getAssignee())) {
            taskQuery = taskQuery.taskAssignee(commonTask.getAssignee());
        }
        if (StringUtils.isNotEmpty(commonTask.getId())) {
            taskQuery = taskQuery.taskId(commonTask.getId());
        }
        if (StringUtils.isNotEmpty(commonTask.getName())) {
            taskQuery = taskQuery.taskName(commonTask.getName());
        }
        if (StringUtils.isNotEmpty(commonTask.getTaskDefinitionKey())) {
            taskQuery = taskQuery.taskDefinitionKey(commonTask.getTaskDefinitionKey());
        }
        if (StringUtils.isNotEmpty(commonTask.getCategory())) {
            taskQuery = taskQuery.taskCategory(commonTask.getCategory());
        }
        if (StringUtils.isNotEmpty(commonTask.getDescription())) {
            taskQuery = taskQuery.taskCategory(commonTask.getCategory());
        }
        if (StringUtils.isNotEmpty(commonTask.getExecutionId())) {
            taskQuery = taskQuery.taskCategory(commonTask.getExecutionId());
        }
        if (map != null && !map.isEmpty()) {
            taskQuery = taskQuery.taskVariableValueEquals(map);
        }
        if (StringUtils.isNotEmpty(commonTask.getOwner())) {
            taskQuery = taskQuery.taskOwner(commonTask.getOwner());
        }
        if (StringUtils.isNotEmpty(commonTask.getParentTaskId())) {
            taskQuery = taskQuery.taskParentTaskId(commonTask.getParentTaskId());
        }
        long total = taskQuery.count();
        List<Task> taskList = taskQuery.listPage((webPageInfo.getPageNum() - 1) * webPageInfo.getPageSize(), webPageInfo.getPageSize());
        List<CommonTask> list = new LinkedList<>();
        for (Task task : taskList) {
            CommonTask resultTask = new CommonTask();
            BeanUtils.copyProperties(task, resultTask);
            String tableName= (String) taskService.getVariable(task.getId(),"tableName" );
            resultTask.setTableName(tableName);
            list.add(resultTask);
        }
        ResultPageInfo<CommonTask> resultPageInfo = new ResultPageInfo<>();
        resultPageInfo.setTotal(total);
        resultPageInfo.setList(list);
        return resultPageInfo;
    }

    /**
     * Description: 任务认领(加锁)
     *
     * @param taskId
     * @param userId
     */
    public synchronized boolean claimTask(String taskId, String userId) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        EmptyCheckUtils.checkWithException(userId, "userId不能为空！");
        try {
            taskService.claim(taskId, userId);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("任务不能加锁(taskId:" + taskId + ")");
        }
    }

    /**
     * Description: 任务解锁
     *
     * @param taskId 任务Id
     */
    public synchronized boolean unClaimTask(String taskId) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        try {
            taskService.claim(taskId, null);
            return true;
        } catch (Exception e) {
            throw new ServiceException("任务解锁失败，任务Id:" + taskId);
        }
    }

    /**
     * 完成任务
     *
     * @return
     */
    public boolean completeTask(String taskId, Map<String, Object> map) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        try {
            if (map == null || map.isEmpty()) {
                taskService.complete(taskId);
            } else {
                taskService.complete(taskId, map);
            }
            return true;
        } catch (Exception e) {
            throw new ServiceException("任务提交失败");
        }
    }

    /**
     * 设置候选人（可设置多个）
     *
     * @param taskId
     * @param userIds
     */
    public boolean addCandidateUser(String taskId, String userIds) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        EmptyCheckUtils.checkWithException(userIds, "userIds不能为空！");
        try {
            taskService.addCandidateUser(taskId, userIds);
            return true;
        } catch (Exception e) {
            throw new ServiceException("设置候选人失败");
        }
    }

    /**
     * 更新候选人
     *
     * @param taskId
     * @param userIds
     * @return
     */
    public boolean updateCandidateUser(String taskId, String userIds) {
        EmptyCheckUtils.checkWithException(taskId, "taskId不能为空！");
        EmptyCheckUtils.checkWithException(userIds, "userIds不能为空！");
        try {
            taskService.addCandidateUser(taskId, null);
            taskService.addCandidateUser(taskId, userIds);
            return true;
        } catch (Exception e) {
            throw new ServiceException("更新候选人失败");
        }
    }

    /**
     * 校验名称是否唯一
     *
     * @param name name
     * @return
     */
    public boolean uniqueName(String name) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionName(name).list();
        return list == null || list.size() == 0;
    }

    /**
     * 校验key是否唯一
     *
     * @param key
     * @return
     */
    public boolean uniqueKey(String key) {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        return list == null || list.size() == 0;
    }

    /**
     * 删除未完成实例
     * @param id id主键
     * @param reason 删除原因
     * @param flag 是否删除历史记录
     * @return
     */
    public boolean deleteProcessInstance(String id, String reason, boolean flag){
        try {
            runtimeService.deleteProcessInstance(id,reason);
            if (flag) {
                historyService.deleteHistoricProcessInstance(id);
            }
            return true;
        } catch (Exception e) {
            logger.error("删除流程失败",e);
            throw new ServiceException("删除流程失败",e);
        }
    }

    public void rollBackToAssignWoekFlow(String processInstanceId, String destTaskkey) {
//        // 取得当前任务.当前任务节点
//        destTaskkey ="usertask2";
//         HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//        Map<String, Object> variables;
        ExecutionEntity entity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();
//        ProcessDefinitionEntity definition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService)
//                .getDeployedProcessDefinition(entity.getProcessDefinitionId());
//        variables = entity.getProcessVariables();
//        //当前活动环节
//        ActivityImpl currActivityImpl = definition.findActivity(entity.getActivityId());
//        //目标活动节点
//        ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(destTaskkey);
//        if(currActivityImpl !=null){
//            //所有的出口集合
//            List<PvmTransition> pvmTransitions = currActivityImpl.getOutgoingTransitions();
//            List<PvmTransition> oriPvmTransitions = new ArrayList<PvmTransition>();
//            for(PvmTransition transition : pvmTransitions){
//                oriPvmTransitions.add(transition);
//            }
//            //清除所有出口
//            pvmTransitions.clear();
//            //建立新的出口
//            List<TransitionImpl> transitionImpls = new ArrayList<TransitionImpl>();
//            TransitionImpl tImpl = currActivityImpl.createOutgoingTransition();
//            tImpl.setDestination(nextActivityImpl);
//            transitionImpls.add(tImpl);
//
//            List<Task> list = taskService.createTaskQuery().processInstanceId(entity.getProcessInstanceId())
//                    .taskDefinitionKey(entity.getActivityId()).list();
//            for(Task task:list){
//                taskService.complete(task.getId(), variables);
//                historyService.deleteHistoricTaskInstance(task.getId());
//            }
//
//            for(TransitionImpl transitionImpl:transitionImpls){
//                currActivityImpl.getOutgoingTransitions().remove(transitionImpl);
//            }
//
//            for(PvmTransition pvmTransition:oriPvmTransitions){
//                pvmTransitions.add(pvmTransition);
//            }
//        }
    }

    /**
     * 删除一个历史流程
     * @param instansId
     * @return
     */
    public boolean deleteProcessInstanceHistory(String instansId) {
        try {
            historyService.deleteHistoricProcessInstance(instansId);
            return true;
        } catch (Exception e) {
            throw new ServiceException("删除流程历史记录失败！", e);
        }
    }
}
