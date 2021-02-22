package com.hcframe.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (BackReason)实体类
 *
 * @author lhc
 * @since 2020-09-04 09:54:13
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class BackReason implements Serializable {
    private static final long serialVersionUID = -15171833204492182L;

    private Integer backId;

    private String taskId;

    private String reason;

    private String title;

}
