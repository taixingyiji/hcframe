package com.taixingyiji.base.module.datasource.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (DatasourceType)实体类
 *
 * @author lhc
 * @since 2020-09-28 15:18:29
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@ApiModel
public class DatasourceType implements Serializable {
    private static final long serialVersionUID = 331087641518717829L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer typeId;

    @ApiModelProperty(
            value = "Key",
            dataType = "String"
    )
    private String typeKey;

    @ApiModelProperty(
            value = "名称",
            dataType = "String"
    )
    private String typeValue;

    @ApiModelProperty(
            value = "驱动类地址",
            dataType = "String"
    )
    private String driver;

    @ApiModelProperty(
            value = "数据库校验语句",
            dataType = "String"
    )
    private String validateQuery;


}
