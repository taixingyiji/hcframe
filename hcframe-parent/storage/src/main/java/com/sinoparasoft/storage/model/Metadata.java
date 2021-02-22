package com.sinoparasoft.storage.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 文件元数据类
 *
 * @author 袁涛
 * @date 2017.10.16
 */
public class Metadata {
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
	@JsonProperty("create_time")
	private Date createTime;
	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
	@JsonProperty("modified_time")
	private Date modifiedTime;
	/**
	 * 文件长度，单位：字节
	 */
	private Long length;

	/**
	 * 文件标签，版本
	 */
	private String tag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "Metadata [name=" + name + ", createTime=" + createTime + ", modifiedTime=" + modifiedTime + ", length="
				+ length + ", tag=" + tag + "]";
	}

}
