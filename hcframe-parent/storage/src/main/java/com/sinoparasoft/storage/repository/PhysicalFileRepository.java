package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.PhysicalFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 物理文件管理接口
 *
 * @author 袁涛
 * @date 2017.10.26
 */
public interface PhysicalFileRepository extends JpaRepository<PhysicalFile, Long> {
	/**
	 * 根据文件MD5和SHA统计物理文件个数
	 *
	 * @param md5
	 *            文件MD5
	 * @param sha
	 *            文件SHA
	 * @return 物理文件个数
	 */
	public Long countByMd5AndSha(String md5, String sha);

	/**
	 * 根据文件MD5和SHA查找物理文件信息
	 *
	 * @param md5
	 *            文件MD5
	 * @param sha
	 *            文件SHA
	 * @return 物理文件信息
	 */
	public PhysicalFile findFirstByMd5AndSha(String md5, String sha);

	/**
	 * 根据文件编号统计物理文件的个数
	 *
	 * @param uuid
	 *            文件编号
	 * @return 物理文件的个数
	 */
	public Long countByUuid(String uuid);

	/**
	 * 根据文件编号查询物理文件信息
	 *
	 * @param uuid
	 *            文件编号
	 * @return 物理文件信息
	 */
	public PhysicalFile findFirstByUuid(String uuid);

}
