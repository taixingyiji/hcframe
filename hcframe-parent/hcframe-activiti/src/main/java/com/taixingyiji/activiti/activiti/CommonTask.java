package com.taixingyiji.activiti.activiti;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

/**
 * @author lhc
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CommonTask {

    private String id;

    private String name;

    private String description;

    private Integer priority;

    private String owner;

    private String assignee;

    private String processInstanceId;

    private String executionId;

    private String processDefinitionId;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String taskDefinitionKey;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueDate;

    private String category;

    private String parentTaskId;

    private String tenantId;

    private String formKey;

    private Map<String, Object> taskLocalVariables;

    private Map<String, Object> processVariables;

    private Date claimTime;

    @Builder.Default
    private Boolean isUnAssignee = false;

    private String tableName;

}
