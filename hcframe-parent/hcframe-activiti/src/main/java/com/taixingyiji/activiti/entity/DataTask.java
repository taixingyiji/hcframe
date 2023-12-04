package com.taixingyiji.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (DataTask)实体类
 *
 * @author lhc
 * @since 2020-09-03 17:39:38
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class DataTask implements Serializable {
    private static final long serialVersionUID = -69233663931357928L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer dataTaskId;

    private String tableName;

    private String tableId;

    private String taskId;

    private String taskKey;

    private String taskName;

    private String processId;

    private Integer isBack;

    private String assignee;

}
