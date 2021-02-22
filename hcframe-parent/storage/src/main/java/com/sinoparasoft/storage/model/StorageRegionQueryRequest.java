package com.sinoparasoft.storage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 物理存储区查询条件
 *
 * @author 袁涛
 * @date 2018.09.13
 */
public class StorageRegionQueryRequest {

	@ApiModelProperty(value = "物理存储区名称")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
