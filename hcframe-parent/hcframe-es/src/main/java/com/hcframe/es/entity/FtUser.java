package com.hcframe.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * (FtUser)实体类
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FtUser implements Serializable {
    private static final long serialVersionUID = 115625188750279606L;
    /**
    * 主键ID
    */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "select USER_ID.nextval from dual")
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
    * 用户类型
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
