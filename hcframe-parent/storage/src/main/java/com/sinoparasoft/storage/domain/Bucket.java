package com.sinoparasoft.storage.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 逻辑存储区实体类
 *
 * @author 袁涛
 * @date 2018.06.07
 */
@Entity
@Table(name = "bucket")
public class Bucket {
	/**
	 * 编号
	 */
	@ApiModelProperty(value = "编号")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	/**
	 * 版本
	 */
	@ApiModelProperty(value = "版本", hidden = true)
	@Version
	@Column(name = "version")
	private Integer version;

	/**
	 * 逻辑存储区名称
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "name")
	private String name;

	/**
	 * 逻辑存储区描述
	 */
	@Size(max = 64)
	@Column(name = "description")
	private String description;

	/**
	 * 创建时间
	 */
	@NotNull
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@NotNull
	@Column(name = "modified_time")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date modifiedTime;

	/**
	 * 元数据，JSON格式
	 */
	@Size(max = 1024)
	@Column(name = "metadata")
	private String metadata;

	/**
	 * 容量，单位：字节
	 */
	@Column(name = "total_size")
	private Long totalSize;

	/**
	 * 已使用容量，单位：字节
	 */
	@Column(name = "used_size")
	private Long usedSize;

	/**
	 * 剩余容量，单位：字节
	 */
	@Column(name = "available_size")
	private Long availableSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public Long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(Long usedSize) {
		this.usedSize = usedSize;
	}

	public Long getAvailableSize() {
		return availableSize;
	}

	public void setAvailableSize(Long availableSize) {
		this.availableSize = availableSize;
	}

	@Override
	public String toString() {
		return "Bucket [id=" + id + ", version=" + version + ", name=" + name + ", description=" + description
				+ ", createTime=" + createTime + ", modifiedTime=" + modifiedTime + ", metadata=" + metadata + ", totalSize="
				+ totalSize + ", usedSize=" + usedSize + ", availableSize=" + availableSize + "]";
	}

}
