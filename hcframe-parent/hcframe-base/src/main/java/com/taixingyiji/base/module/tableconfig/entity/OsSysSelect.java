package com.taixingyiji.base.module.tableconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * (OsSysSelect)实体类
 *
 * @author lhc
 * @since 2020-03-17 17:42:34
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "OS_SYS_SELECT")
public class OsSysSelect implements Serializable {
    private static final long serialVersionUID = 235289253065563954L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer selectId;

    private String selectKey;

    private String selectValue;

    private Integer selectType;

    private Integer fieldId;


}
