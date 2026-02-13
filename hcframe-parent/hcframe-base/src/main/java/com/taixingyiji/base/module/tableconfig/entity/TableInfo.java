package com.taixingyiji.base.module.tableconfig.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lhc
 * @date 2020-10-16
 * @description 表信息实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class TableInfo implements Serializable {
    private static final long serialVersionUID = -277016779002661446L;

    @ApiModelProperty(
            value = "表名",
            dataType = "String"
    )
    private String tableName;

    @ApiModelProperty(
            value = "表描述",
            dataType = "String"
    )
    @Builder.Default
    private String tableComment = "";

}
