package com.hcframe.base.module.datasource.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * (DatasourceConfig)实体类
 *
 * @author lhc
 * @since 2020-09-23 09:28:03
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@ApiModel
public class DatasourceConfig implements Serializable {
    private static final long serialVersionUID = 188640098731075764L;

    public static final int ENABLE = 1;
    public static final int DISABLE = 0;
    public static final int DEFAULT = 1;
    public static final int UNDEFAULT = 0;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer datasourceId;

    @ApiModelProperty(
            value = "驱动包地址",
            dataType = "String"
    )
    private String driverClassName;

    @ApiModelProperty(
            value = "数据库连接url",
            example = "",
            dataType = "String"
    )
    private String url;

    @ApiModelProperty(
            value = "数据库用户名",
            dataType = "String"
    )
    private String username;

    @ApiModelProperty(
            value = "数据库密码",
            dataType = "String"
    )
    private String password;

    @ApiModelProperty(
            value = "数据库key(数据库别名)",
            example = "",
            dataType = "String"
    )
    private String commonAlias;

    @ApiModelProperty(
            value = "数据库类型",
            example = "mysql",
            dataType = "String"
    )
    private String commonType;

    @ApiModelProperty(
            value = "是否启用数据库",
            example = "1",
            dataType = "Integer"
    )
    private Integer sysEnabled;

    @ApiModelProperty(
            value = "是否是默认数据库",
            example = "1",
            dataType = "Integer"
    )
    private Integer isDefault;

    @ApiModelProperty(
            value = "数据库库名",
            dataType = "String"
    )
    private String schemaName;

    @ApiModelProperty(
            value = "数据库中文描述",
            dataType = "String"
    )
    private String sysDescription;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
