package com.sinoparasoft.storage.model;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件分块上传请求
 *
 * @author 袁涛
 * @date 2018.09.14
 */
public class FileBlockCreateRequest {
	/**
	 * 逻辑存储区名称
	 */
	@ApiModelProperty(value = "逻辑存储区名称")
	private String bucketName;
	/**
	 * 文件内容
	 */
	@ApiModelProperty(value = "文件内容")
	private MultipartFile file;
	/**
	 * 逻辑文件编号
	 */
	@ApiModelProperty(value = "逻辑文件编号")
	private String uuid;
	/**
	 * 逻辑文件名称
	 */
	@ApiModelProperty(value = "逻辑文件名称")
	private String name;
	/**
	 * 逻辑文件长度
	 */
	@ApiModelProperty(value = "逻辑文件长度")
	private Long length;
	/**
	 * 逻辑文件分块序号
	 */
	@ApiModelProperty(value = "逻辑文件分块序号")
	private Integer chunk;
	/**
	 * 逻辑文件分块数量
	 */
	@ApiModelProperty(value = "逻辑文件分块数量")
	private Integer chunks;

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Integer getChunk() {
		return chunk;
	}

	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}

	public Integer getChunks() {
		return chunks;
	}

	public void setChunks(Integer chunks) {
		this.chunks = chunks;
	}

}
