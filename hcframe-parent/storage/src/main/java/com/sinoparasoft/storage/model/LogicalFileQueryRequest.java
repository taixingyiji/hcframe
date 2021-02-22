package com.sinoparasoft.storage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 文件查询条件
 *
 * @author 袁涛
 * @date 2018.09.14
 */
public class LogicalFileQueryRequest {

	@ApiModelProperty(value = "文件名称")
	private String name;

	@ApiModelProperty(value = "逻辑存储区名称")
	private String bucketName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
}
