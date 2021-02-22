package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.PhysicalTileFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 物理分块文件管理接口
 *
 * @author 袁涛
 * @date 2017.11.09
 */
public interface PhysicalTileFileRepository extends JpaRepository<PhysicalTileFile, Long> {
	/**
	 * 根据文件MD5和SHA统计物理分块文件个数
	 *
	 * @param md5
	 *            物理分块文件MD5
	 * @param sha
	 *            物理分块文件SHA
	 * @return 物理文件个数
	 */
	public Long countByMd5AndSha(String md5, String sha);

	/**
	 * 根据文件MD5和SHA查找物理分块文件信息
	 *
	 * @param md5
	 *            物理分块文件MD5
	 * @param sha
	 *            物理分块文件SHA
	 * @return 物理分块文件信息
	 */
	public PhysicalTileFile findFirstByMd5AndSha(String md5, String sha);

	/**
	 * 根据文件编号统计物理分块文件的个数
	 *
	 * @param uuid
	 *            物理分块文件编号
	 * @return 物理分块文件的个数
	 */
	public Long countByUuid(String uuid);

	/**
	 * 根据文件编号查询物理分块文件信息
	 *
	 * @param uuid
	 *            物理分块文件编号
	 * @return 物理分块文件信息
	 */
	public PhysicalTileFile findFirstByUuid(String uuid);

	/**
	 * 根据物理文件编号统计物理分块文件的个数
	 *
	 * @param uuid
	 *            物理文件编号
	 * @return 物理分块文件的个数
	 */
	public Long countByPhysicalUuid(String physicalUuid);

	/**
	 * 根据物理文件编号查询物理分块文件列表
	 *
	 * @param uuid
	 *            物理文件编号
	 * @return 物理分块文件列表
	 */
	public List<PhysicalTileFile> findByPhysicalUuidOrderByTile(String physicalUuid);

	/**
	 * 根据存储区编号统计物理分块文件的个数
	 *
	 * @param storageRegionUuid
	 *            存储区编号
	 * @return 物理分块文件的个数
	 */
	public Long countByStorageRegionUuid(String storageRegionUuid);
}
