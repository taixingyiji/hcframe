package com.taixingyiji.activiti;

import com.alibaba.fastjson.JSONObject;
import com.taixingyiji.activiti.activiti.ActivitiUtils;
import com.taixingyiji.activiti.activiti.CommonTask;
import com.taixingyiji.activiti.controller.TaskController;
import com.hcframe.base.common.ResultPageInfo;
import com.hcframe.base.common.WebPageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Component
public class TaskControllerTest {

    @Autowired
    TaskController taskController;

    @Autowired
    ActivitiUtils activitiUtils;

    @Test
    public void startTask() {
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", "user");
        map.put("tableId", 10);
        map.put("approvalPerson", "lhc");
//        taskController.startTaskByKey("test", map);
    }

    @Test
    public void getTaskList(){
        CommonTask commonTask = new CommonTask();
        commonTask.setName("test");
        commonTask.setAssignee("lhc");
        ResultPageInfo<CommonTask> list =activitiUtils.getTaskList(commonTask, WebPageInfo.builder().pageNum(1).pageSize(10).build(), Collections.emptyMap());
        List<CommonTask> commonTaskList=list.getList();
        for (CommonTask commonTask1 : commonTaskList) {
            System.out.println(commonTask1.getId());
            System.out.println(commonTask1.getTaskDefinitionKey());
            System.out.println(commonTask1.getName());
            System.out.println(commonTask1.getAssignee());
        }
    }

    @Test
    public void completeTask(){
        Map<String, Object> map = new HashMap<>();
        map.put("day", 3);
        String data = JSONObject.toJSONString(map);
        taskController.commitTask("6e49a42d-ee6c-11ea-846d-f20e832dd5e1", data,false,null);
    }
    @Test
    public void complete2Task(){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        String data = JSONObject.toJSONString(map);
        taskController.commitTask("c3c3f756-ee6c-11ea-b7c9-f20e832dd5e1", data,false,null);
    }

    @Test
    public void complete3Task(){
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        String data = JSONObject.toJSONString(map);
        taskController.commitTask("c3cc82e0-ee6c-11ea-b7c9-f20e832dd5e1", data,false,null);
    }


    @Test
    public void deleteTask(){
        taskController.deleteTaskInstans("2d45a8a8-ee47-11ea-8baf-f20e832dd5e1","",true);
    }

}
