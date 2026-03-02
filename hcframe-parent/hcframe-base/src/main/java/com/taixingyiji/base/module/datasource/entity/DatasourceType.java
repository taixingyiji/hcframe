package com.taixingyiji.base.module.datasource.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema
public class DatasourceType implements Serializable {
    private static final long serialVersionUID = 331087641518717829L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer typeId;

    @Schema(description = "Key")
    private String typeKey;

    @Schema(description = "名称")
    private String typeValue;

    @Schema(description = "驱动类地址")
    private String driver;

    @Schema(description = "数据库校验语句")
    private String validateQuery;


}
