package com.sinoparasoft.storage.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 物理分块文件实体类
 *
 * @author 袁涛
 * @date 2017.11.09
 */
@Entity
@Table(name = "physical_tile_file")
public class PhysicalTileFile {
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
	 * 文件编号
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "uuid")
	private String uuid;

	/**
	 * 物理文件编号
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "physical_uuid")
	private String physicalUuid;

	/**
	 * 物理分块文件序号
	 */
	@NotNull
	@Column(name = "tile")
	private Integer tile;

	/**
	 * 文件路径
	 */
	@NotNull
	@Size(min = 1, max = 256)
	@Column(name = "path")
	private String path;

	/**
	 * 文件MD5
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "md5")
	private String md5;

	/**
	 * 文件SHA
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "sha")
	private String sha;

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
	 * 文件长度，单位：字节
	 */
	@NotNull
	@Column(name = "length")
	private Long length;

	/**
	 * 存储区编号
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "storage_region_uuid")
	private String storageRegionUuid;

	/**
	 * 校验文件是否健康
	 */
	@NotNull
	@Column(name = "healthy")
	private Boolean healthy;

	/**
	 * 校验时间
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

	public String getPhysicalUuid() {
		return physicalUuid;
	}

	public void setPhysicalUuid(String physicalUuid) {
		this.physicalUuid = physicalUuid;
	}

	public Integer getTile() {
		return tile;
	}

	public void setTile(Integer tile) {
		this.tile = tile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
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

	public String getStorageRegionUuid() {
		return storageRegionUuid;
	}

	public void setStorageRegionUuid(String storageRegionUuid) {
		this.storageRegionUuid = storageRegionUuid;
	}

	public Boolean getHealthy() {
		return healthy;
	}

	public void setHealthy(Boolean healthy) {
		this.healthy = healthy;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Override
	public String toString() {
		return "PhysicalTileFile [id=" + id + ", version=" + version + ", uuid=" + uuid + ", physicalUuid="
				+ physicalUuid + ", tile=" + tile + ", path=" + path + ", md5=" + md5 + ", sha=" + sha + ", createTime="
				+ createTime + ", modifiedTime=" + modifiedTime + ", length=" + length + ", storageRegionUuid="
				+ storageRegionUuid + ", healthy=" + healthy + ", checkTime=" + checkTime + "]";
	}

}
