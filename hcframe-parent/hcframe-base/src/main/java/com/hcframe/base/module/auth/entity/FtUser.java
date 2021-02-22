package com.hcframe.base.module.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (FtUser)实体类
 *
 * @author lhc
 * @since 2020-09-23 13:49:30
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Table(name = "FT_USER")
public class FtUser implements Serializable {
    private static final long serialVersionUID = -44415286580890701L;
    /**
    * 主键ID
    */
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer userId;
    /**
    * 用户名
    */
    private String username;
    /**
    * 密码
    */
    private String password;
    /**
    * 手机号
    */
    private String phone;
    /**
    * 用户类型,0未验证用户，1外部，2内部，3企业
    */
    private Integer userType;
    /**
    * 启用/禁用
    */
    private Integer enabled;
    /**
    * 用户信息主键
    */
    private Integer userInfoId;
    /**
    * 创建时间
    */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 修改时间
    */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer orgId;

}
