package com.sinoparasoft.storage.model;

/**
 * 文件上传响应类
 *
 * @author 袁涛
 * @date 2017.10.16
 */
public class FileCreateResponse {
	/**
	 * 响应状态，是否成功
	 */
	private Boolean status;

	/**
	 * 文件编号
	 */
	private String uuid;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "FileCreateResponse [status=" + status + ", uuid=" + uuid + "]";
	}

}
