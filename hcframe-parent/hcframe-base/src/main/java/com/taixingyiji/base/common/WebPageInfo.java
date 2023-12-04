package com.taixingyiji.base.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * (WebPageInfo)实体类
 *
 * @author lhc
 * @since 2020-02-11 19:29:10
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@ApiModel
public class WebPageInfo implements Serializable {

    private static final long serialVersionUID = 1156251348750279606L;

    public static final String DESC = "desc";
    public static final String ASC = "asc";

    @ApiModelProperty(
            value="每页显示条数" ,
            example="10",
            dataType = "Integer"
    )
    private Integer pageSize = 10;

    @ApiModelProperty(
            value="页码" ,
            example="1",
            dataType = "Integer"
    )
    private Integer pageNum = 1;

    @ApiModelProperty(value="排序字段")
    private String sortField;

    @ApiModelProperty(
            value = "正序倒序",
            allowableValues = "asc,desc",
            example = "asc")
    private String order = ASC;

    public static boolean hasSort(WebPageInfo webPageInfo) {
        return !StringUtils.isBlank(webPageInfo.getSortField());
    }

    public String getSortSql() {
        return this.sortField + " " + this.order;
    }
}
