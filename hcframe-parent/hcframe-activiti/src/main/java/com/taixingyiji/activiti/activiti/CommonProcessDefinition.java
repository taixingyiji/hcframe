package com.taixingyiji.activiti.activiti;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lhc
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CommonProcessDefinition {

    public  static String BPMN = "bpmn";

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private Integer version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private String tenantId;

    private String engineVersion;
}
