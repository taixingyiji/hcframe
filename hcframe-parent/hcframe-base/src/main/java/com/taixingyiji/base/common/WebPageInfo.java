package com.taixingyiji.base.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

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
@Schema
public class WebPageInfo implements Serializable {

    private static final long serialVersionUID = 1156251348750279606L;

    public static final String DESC = "desc";
    public static final String ASC = "asc";

    @Schema(
            description="每页显示条数" ,
            example="10"
    )
    @Builder.Default
    private Integer pageSize = 10;

    @Schema(
            description="页码" ,
            example="1"
    )
    @Builder.Default
    private Integer pageNum = 1;

    @Schema(description="排序字段")
    private String sortField;

    @Schema(
            description = "正序倒序",
            allowableValues = {"asc","desc"},
            example = "asc")
    @Builder.Default
    private String order = ASC;


    @Schema(
            description = "符合排序字段",example = "[{\"field\":\"CREATE_TIME\",\"order\":\"ASC\"}]")
    private String sortList;

    @Schema(
            description = "开启分页缓存"
    )
    @Builder.Default
    private boolean enableCache = false;

    @Schema(
            description = "开启分页排序"
    )
    @Builder.Default
    private boolean enableSort = true;

    public static boolean isSafeOrderBy(String orderBy) {
        if (StringUtils.isBlank(orderBy)) {
            return true;
        }
        // 忽略大小写
        return orderBy.matches("(?i)^(asc|desc)$");
    }

    public static boolean hasSortList(WebPageInfo webPageInfo) {
        return !StringUtils.isBlank(webPageInfo.getSortList());
    }

    public static boolean hasSort(WebPageInfo webPageInfo) {
        return !StringUtils.isBlank(webPageInfo.getSortField());
    }

    public String getSortSql() {
        if(!isSafeOrderBy(this.order)){
            throw new ServiceException("排序字段不合法");
        }
        return this.sortField + " " + this.order;
    }
}
