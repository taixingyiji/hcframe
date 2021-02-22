package com.sinoparasoft.storage.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 逻辑文件实体类
 *
 * @author 袁涛
 * @date 2017.10.26
 */
@Entity
@Table(name = "logical_file")
public class LogicalFile {
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
	 * 文件名称
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "name")
	private String name;

	/**
	 * 文件类型
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "type")
	private String type;

	/**
	 * 物理文件编号
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "physical_uuid")
	private String physicalUuid;

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
	 * 文件长度，单位：字节
	 */
	@Column(name = "length")
	private Long length;

	/**
	 * 文件标签，版本
	 */
	@Size(max = 32)
	@Column(name = "tag")
	private String tag;

	/**
	 * 逻辑存储区名称
	 */
	@NotNull
	@Size(min = 1, max = 64)
	@Column(name = "bucket_name")
	private String bucketName;

	/**
	 * 是否公开
	 */
	@Column(name = "open")
	private Boolean open;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhysicalUuid() {
		return physicalUuid;
	}

	public void setPhysicalUuid(String physicalUuid) {
		this.physicalUuid = physicalUuid;
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

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	@Override
	public String toString() {
		return "LogicalFile [id=" + id + ", version=" + version + ", uuid=" + uuid + ", name=" + name + ", type=" + type
				+ ", physicalUuid=" + physicalUuid + ", createTime=" + createTime + ", modifiedTime=" + modifiedTime
				+ ", metadata=" + metadata + ", length=" + length + ", tag=" + tag + ", bucketName=" + bucketName
				+ ", open=" + open + "]";
	}

}
