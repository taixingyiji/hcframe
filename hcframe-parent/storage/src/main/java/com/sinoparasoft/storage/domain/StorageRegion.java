package com.sinoparasoft.storage.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 存储区实体类
 *
 * @author 袁涛
 * @date 2017.10.26
 */
@Entity
@Table(name = "storage_region")
public class StorageRegion {
	/**
	 * 编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	/**
	 * 版本
	 */
	@Version
	@Column(name = "version")
	private Integer version;

	/**
	 * 存储区编号
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "uuid")
	private String uuid;

	/**
	 * 存储区名称
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "name")
	private String name;

	/**
	 * 存储区描述
	 */
	@Size(max = 64)
	@Column(name = "description")
	private String description;

	/**
	 * 存储区路径
	 */
	@NotNull
	@Size(min = 1, max = 256)
	@Column(name = "path")
	private String path;

	/**
	 * 存储区总容量，单位：字节
	 */
	@NotNull
	@Column(name = "total_size")
	private Long totalSize;

	/**
	 * 存储区已用容量，单位：字节
	 */
	@NotNull
	@Column(name = "used_size")
	private Long usedSize;

	/**
	 * 存储区可用容量，单位：字节
	 */
	@NotNull
	@Column(name = "available_size")
	private Long availableSize;

	/**
	 * 检查存储区是否健康
	 */
	@NotNull
	@Column(name = "healthy")
	private Boolean healthy;

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
	 * 检查时间
	 */
	@NotNull
	@Column(name = "check_time")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date checkTime;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public Boolean getHealthy() {
		return healthy;
	}

	public void setHealthy(Boolean healthy) {
		this.healthy = healthy;
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

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
}
