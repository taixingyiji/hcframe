package com.taixingyiji.activiti.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("工具Process")
public class CommonProcessDefinition {

    public  static String BPMN = "bpmn";

    @ApiModelProperty(hidden = true)
    private String id;

    @ApiModelProperty(hidden = true)
    private String category;

    @ApiModelProperty(
            value="流程Process名称"
    )
    private String name;

    @ApiModelProperty(
            value="流程Process的KEY"
    )
    private String key;

    @ApiModelProperty(hidden = true)
    private String description;

    @ApiModelProperty(
            value="版本"
    )
    private Integer version;

    @ApiModelProperty(hidden = true)
    private String resourceName;

    @ApiModelProperty(hidden = true)
    private String deploymentId;

    @ApiModelProperty(hidden = true)
    private String diagramResourceName;

    @ApiModelProperty(hidden = true)
    private String tenantId;

    @ApiModelProperty(hidden = true)
    private String engineVersion;
}
