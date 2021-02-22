package com.hcframe.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (DataTaskHis)实体类
 *
 * @author lhc
 * @since 2020-09-04 08:58:42
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class DataTaskHis implements Serializable {
    private static final long serialVersionUID = -66890927690522795L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer hisTaskId;

    private String tableName;

    private String tableId;

    private String taskId;

    private String taskKey;

    private String processId;

    private String taskName;

    private Integer isBack;

    private String assignee;


}
