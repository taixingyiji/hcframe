package com.hcframe.base.module.tableconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * (OsSysField)实体类
 *
 * @author lhc
 * @since 2020-03-17 17:26:53
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "OS_SYS_FIELD")
public class OsSysField implements Serializable {
    private static final long serialVersionUID = 402012528145507031L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer fieldId;

    private String fieldName;

    private String nameCn;

    private String javaType;

    private String webType;
    /**
     * 校验规则
     */
    private String notNull;
    /**
     * 是否只读
     */
    private Integer isChange;
    /**
     * 是否表头
     */
    private Integer isHead;
    /**
     * 是否是外键
     */
    private Integer isFk;


    private Integer tableId;
    /**
     * 对应外键的表别名
     */
    private String fkTable;
    /**
     * Java实体类字段
     */
    private String javaField;

    private Integer isSearch;

    private Integer isDetails;

    private Integer isForm;

    private Integer isBatch;

    @ColumnType(column = "FIELD_SIGN")
    private Integer sign;

    private Integer logic;
    private String fkKey;
    private String fkValue;
    private Integer width;
    private Integer orderId;

    private List<OsSysSelect> selectList;
}
