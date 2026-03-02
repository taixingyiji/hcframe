package com.taixingyiji.base.module.tableconfig.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "字段名")
    private String columnName;

    @Schema(description = "数据类型")
    private String dataType;

    @Schema(description = "键类型,1为主键，0为非主键", allowableValues = {"1"})
    private Integer columnKey;

    @Schema(description = "注释")
    private String columnComment;
}
