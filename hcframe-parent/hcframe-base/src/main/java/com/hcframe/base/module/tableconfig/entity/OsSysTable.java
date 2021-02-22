package com.hcframe.base.module.tableconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "OS_SYS_TABLE")
public class OsSysTable implements Serializable {


    private static final long serialVersionUID = -1043846202777180979L;
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer tableId;

    private String tableName;

    private String tableAlias;

    private String tablePk;

    private String tableContent;

    private List<OsSysField> fieldList;
}
