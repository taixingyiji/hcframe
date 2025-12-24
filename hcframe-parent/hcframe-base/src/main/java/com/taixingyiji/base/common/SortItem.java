package com.taixingyiji.base.common;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @className SortItem
 * @author lhc
 * @date 2025年12月24日 11:16
 * @description 描述
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@ApiModel
public class SortItem implements Serializable {
    private static final long serialVersionUID = 5462627800342658554L;
    private String field;
    private String order;
}
