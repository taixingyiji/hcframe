package com.hcframe.base.module.shiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (FtToken)实体类
 *
 * @author lhc
 * @since 2020-02-11 20:26:15
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "FT_TOKEN")
public class FtToken implements Serializable {
    private static final long serialVersionUID = 202774093136745829L;

    @Id
    private String tokenId;

    private String token;

    private String userId;

    private Date updateTime;

    private Date expireTime;

}
