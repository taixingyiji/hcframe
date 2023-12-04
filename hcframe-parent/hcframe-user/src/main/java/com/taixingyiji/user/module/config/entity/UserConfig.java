package com.taixingyiji.user.module.config.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lhc
 * @version 1.0
 * @className UserConfig
 * @date 2021年05月26日 10:33 上午
 * @description 描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "OS_SYS_USER_CONFIG")
public class UserConfig implements Serializable {
    private static final long serialVersionUID = 1553490676930061444L;
    // 主键
    @Id
    private Integer userConfigId;

    // 用户表表名
    private String userTableName;

    // 机构表表名
    private String orgTableName;

    // 用户表主键
    private String userKey;

    // 机构表主键
    private String orgKey;

    // 机构表自关联主键，用于生成机构树及从属关系
    private String orgParentId;

    // 用户关联机构字段，暂时支持机构一对用户多，此字段需要在用户表中提现
    private String orgUserCode;

    // 机构表与用户表所关联字段，此字段处于机构表中
    private String orgRelCode;
}
