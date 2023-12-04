package com.taixingyiji.base.module.data.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lhc
 * @decription 前端关联查询格式
 * @date 2020-12-25
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class JoinCondition {
    // 表名
    private String name;
    // 关联查询字段
    private String field;
    // 与左表关联查询字段
    private String joinField;
    // 关联的父表
    private String fkTable;
}
