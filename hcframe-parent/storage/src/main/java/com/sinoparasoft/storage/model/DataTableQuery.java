package com.sinoparasoft.storage.model;

/**
 * JQuery dataTables 查询参数类
 *
 * @author 袁涛
 * @date 2017.09.29
 */
public class DataTableQuery {
	/**
	 * 起始记录
	 */
	private Integer firstResult;

	/**
	 * 最大记录数
	 */
	private Integer maxResults;

	/**
	 * 全局搜索字段
	 */
	private String search;

	/**
	 * 排序字段名称
	 */
	private String sortFieldName;

	/**
	 * 升序ASC或降序DESC
	 */
	private String sortOrder;

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSortFieldName() {
		return sortFieldName;
	}

	public void setSortFieldName(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "DataTableQuery [firstResult=" + firstResult + ", maxResults=" + maxResults + ", search=" + search
				+ ", sortFieldName=" + sortFieldName + ", sortOrder=" + sortOrder + "]";
	}

}
