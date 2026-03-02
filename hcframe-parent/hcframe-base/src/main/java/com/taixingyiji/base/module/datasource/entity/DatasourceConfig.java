package com.taixingyiji.base.module.datasource.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema
public class DatasourceConfig implements Serializable {
    private static final long serialVersionUID = 188640098731075764L;

    public static final int ENABLE = 1;
    public static final int DISABLE = 0;
    public static final int DEFAULT = 1;
    public static final int UNDEFAULT = 0;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer datasourceId;

    @Schema(description = "驱动包地址")
    private String driverClassName;

    @Schema(description = "数据库连接url", example = "")
    private String url;

    @Schema(description = "数据库用户名")
    private String username;

    @Schema(description = "数据库密码")
    private String password;

    @Schema(description = "数据库key(数据库别名)", example = "")
    private String commonAlias;

    @Schema(description = "数据库类型", example = "mysql")
    private String commonType;

    @Schema(description = "是否启用数据库", example = "1")
    private Integer sysEnabled;

    @Schema(description = "是否是默认数据库", example = "1")
    private Integer isDefault;

    @Schema(description = "数据库库名")
    private String schemaName;

    @Schema(description = "数据库中文描述")
    private String sysDescription;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
