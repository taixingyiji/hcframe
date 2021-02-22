package com.sinoparasoft.storage.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传请求
 *
 * @author 袁涛
 * @date 2018.09.14
 */
public class FileCreateRequest {
	/**
	 * 逻辑存储区名称
	 */
	@ApiModelProperty(value = "逻辑存储区名称")
	private String bucketName;
	/**
	 * 文件数据
	 */
	@ApiModelProperty(value = "文件数据")
	private MultipartFile file;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
