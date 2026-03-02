package com.taixingyiji.base.module.tableconfig.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "表描述")
    @Builder.Default
    private String tableComment = "";

}
