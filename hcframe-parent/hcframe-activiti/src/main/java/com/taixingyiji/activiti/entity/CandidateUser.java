package com.taixingyiji.activiti.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * (CandidateUser)实体类
 *
 * @author lhc
 * @since 2020-09-04 13:29:17
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CandidateUser implements Serializable {
    private static final long serialVersionUID = -32069051744595036L;
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer caId;

    private String taskId;

    private String userId;

}
