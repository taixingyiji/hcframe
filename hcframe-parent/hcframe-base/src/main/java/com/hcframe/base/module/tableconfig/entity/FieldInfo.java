package com.hcframe.base.module.tableconfig.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FieldInfo implements Serializable {
    private static final long serialVersionUID = 3537487795273818036L;

    @ApiModelProperty(
            value = "字段名",
            dataType = "String"
    )
    private String columnName;

    @ApiModelProperty(
            value = "数据类型",
            dataType = "String"
    )
    private String dataType;

    @ApiModelProperty(
            value = "键类型,1为主键，0为非主键",
            dataType = "Integer",
            allowableValues = "1"
    )
    private Integer columnKey;

    @ApiModelProperty(
            value = "注释",
            dataType = "String"
    )
    private String columnComment;
}
