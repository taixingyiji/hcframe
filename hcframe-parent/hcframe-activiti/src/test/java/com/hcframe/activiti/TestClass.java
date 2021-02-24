package com.hcframe.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.ProcessInstanceQueryProperty;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestClass {

    private final static Logger logger = LoggerFactory.getLogger(TestClass.class);

    @Resource
    RuntimeService runtimeService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    /**
     * 部署一个流程
     */
    @Test
    public void deployement(){
        InputStream is = null;
        try {
            is = new FileInputStream( "/Users/mac/Documents/common-frame/gitea/common-activiti/src/main/resources/bpmn/数据采集(数据应用室发起).bpmn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream("start_123",is)
                .name("数据采集")
                .deploy();
        logger.info("流程名称："+deployment.getName());
        logger.info("流程ID："+deployment.getId());
    }


    @Test
    public void getModel(){
        ProcessInstanceQueryProperty queryProperty = new ProcessInstanceQueryProperty("processQuery");
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderBy(queryProperty).asc().list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义ID:"+processDefinition.getId());//流程定义的key+版本+随机生成数
            System.out.println("流程定义名称:"+processDefinition.getName());//对应HelloWorld.bpmn文件中的name属性值
            System.out.println("流程定义的key:"+processDefinition.getKey());//对应HelloWorld.bpmn文件中的id属性值
            System.out.println("流程定义的版本:"+processDefinition.getVersion());//当流程定义的key值相同的情况下，版本升级，默认从1开始
            System.out.println("资源名称bpmn文件:"+processDefinition.getResourceName());
            System.out.println("资源名称png文件:"+processDefinition.getDiagramResourceName());
            System.out.println("部署对象ID:"+processDefinition.getDeploymentId());
            System.out.println("################################");
        }
    }

    /**
     * 开始一个流程实例
     */
    @Test
    public void startProcessInstance(){

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("");
        System.out.println("流程实例ID:"+instance.getId());
        System.out.println("流程定义ID:"+instance.getProcessDefinitionId());
    }

    /**
     * 查询流程任务
     */
    @Test
    public void taskQuery(){
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("task__111")
                .taskAssignee("tom").listPage(0,10);

        if(list!=null && list.size()>0){
            for(Task task:list){
                System.out.println("任务ID:"+task.getId());
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务的创建时间:"+task.getCreateTime());
                System.out.println("任务的办理人:"+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID:"+task.getExecutionId());
                System.out.println("流程定义ID:"+task.getProcessDefinitionId());
                System.out.println("getOwner:"+task.getOwner());
                System.out.println("getCategory:"+task.getCategory());
                System.out.println("getDescription:"+task.getDescription());
                System.out.println("getFormKey:"+task.getFormKey());
                Map<String, Object> map = task.getProcessVariables();
                for (Map.Entry<String, Object> m : map.entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }
                for (Map.Entry<String, Object> m : task.getTaskLocalVariables().entrySet()) {
                    System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                }
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask(){
        //任务ID
        String taskId = "64610e0c-bf5c-11ea-b195-be54a4f8513e";
        taskService.complete(taskId);
        System.out.println("完成任务：任务ID："+taskId);
    }

    /**
     * 历史活动实例查询
     */
    @Test
    public void queryHistoryTask() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery() // 创建历史活动实例查询
                .processInstanceId("645e9d08-bf5c-11ea-b195-be54a4f8513e") // 执行流程实例id
                .orderByTaskCreateTime()
                .asc()
                .list();
        for (HistoricTaskInstance hai : list) {
            System.out.println("活动ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("活动名称：" + hai.getName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());
        }
    }

    @Test
    public void deleteFlow() {
    	repositoryService.deleteDeployment("f71e44b2-bf56-11ea-85b5-be54a4f8513e",true);
    }

}
