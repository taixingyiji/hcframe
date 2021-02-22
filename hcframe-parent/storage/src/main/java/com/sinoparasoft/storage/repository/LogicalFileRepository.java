package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.LogicalFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 逻辑文件管理接口
 *
 * @author 袁涛
 * @date 2017.10.26
 */
public interface LogicalFileRepository extends JpaRepository<LogicalFile, Long> {

	/**
	 * 根据文件编号统计逻辑文件的个数
	 *
	 * @param uuid
	 *            文件编号
	 * @return 逻辑文件的个数
	 */
	public Long countByUuid(String uuid);

	/**
	 * 根据文件编号查询逻辑文件信息
	 *
	 * @param uuid
	 *            文件编号
	 * @return 逻辑文件信息
	 */
	public LogicalFile findFirstByUuid(String uuid);

	/**
	 * 根据文件名称查询文件列表
	 *
	 * @param name
	 *            文件名称
	 * @param pageable
	 *            分页信息
	 * @return 文件列表
	 */
	public Page<LogicalFile> findByNameLike(String name, Pageable pageable);

	/**
	 * 根据逻辑存储区名称统计逻辑文件的个数
	 *
	 * @param bucketName
	 *            逻辑存储区名称
	 * @return 逻辑文件的个数
	 */
	public Long countByBucketName(String bucketName);

	/**
	 * 根据逻辑存储区名称统计逻辑文件的大小
	 *
	 * @param bucketName
	 *            逻辑存储区大小
	 * @return 逻辑文件的个数
	 */
	@Query("SELECT SUM(file.length) FROM LogicalFile file WHERE file.bucketName = ?1")
	public Long sumLengthByBucketName(String bucketName);
}
