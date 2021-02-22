package com.sinoparasoft.storage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 简单响应类
 *
 * @author 袁涛
 *
 */
public class Response {

	@ApiModelProperty(value = "内容")
	private Object content;

	public Response(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
