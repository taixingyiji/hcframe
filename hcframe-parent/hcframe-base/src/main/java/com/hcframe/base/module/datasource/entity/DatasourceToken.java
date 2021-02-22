package com.hcframe.base.module.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (DatasourceToken)实体类
 *
 * @author makejava
 * @since 2020-10-10 10:35:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class DatasourceToken implements Serializable {
    private static final long serialVersionUID = 490365047847182899L;

    @Id
    @GeneratedValue(generator="JDBC")
    private Integer tokenId;

    private String token;

}
