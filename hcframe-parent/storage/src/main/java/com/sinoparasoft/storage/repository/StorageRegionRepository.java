package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.StorageRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 物理存储区管理接口
 *
 * @author 袁涛
 * @date 2017.10.26
 */
public interface StorageRegionRepository extends JpaRepository<StorageRegion, Long> {

	/**
	 * 根据可用空间和存储状态统计物理存储区的个数
	 *
	 * @param availableSize
	 *            物理存储区可用容量，单位：字节
	 * @param healthy
	 *            物理存储区是否健康
	 * @return 物理存储区的个数
	 */
	public Long countByAvailableSizeGreaterThanAndHealthy(Long availableSize, Boolean healthy);

	/**
	 * 根据可用空间和存储状态查询物理存储区列表
	 *
	 * @param availableSize
	 *            物理存储区可用容量，单位：字节
	 * @param healthy
	 *            物理存储区是否健康
	 * @return 物理存储区列表
	 */
	public List<StorageRegion> findByAvailableSizeGreaterThanAndHealthy(Long availableSize, Boolean healthy);

	/**
	 * 根据物理存储区编号统计物理存储区的个数
	 *
	 * @param uuid
	 *            物理存储区编号
	 * @return 物理存储区的个数
	 */
	public Long countByUuid(String uuid);

	/**
	 * 根据物理存储区编号查询物理存储区信息
	 *
	 * @param uuid
	 *            物理存储区编号
	 * @return 物理存储区信息
	 */
	public StorageRegion findFirstByUuid(String uuid);

	/**
	 * 根据物理存储区路径查询物理存储区列表
	 *
	 * @param path
	 *            物理存储区路径
	 * @param pageable
	 *            分页信息
	 * @return 物理存储区列表
	 */
	public Page<StorageRegion> findByPathLike(String path, Pageable pageable);

	/**
	 * 根据物理存储区名称查询物理存储区列表
	 *
	 * @param name
	 *            物理存储区名称
	 * @param pageable
	 *            分页信息
	 * @return 物理存储区列表
	 */
	public Page<StorageRegion> findByNameLike(String name, Pageable pageable);

	/**
	 * 根据物理存储区名称统计物理存储区个数
	 *
	 * @param name
	 *            物理存储区名称
	 * @return 物理存储区个数
	 */
	public Long countByName(String name);

	/**
	 * 根据物理存储区编号和物理存储区名称统计物理存储区个数
	 *
	 * @param id
	 *            物理存储区编号
	 * @param name
	 *            物理存储区名称
	 * @return 物理存储区个数
	 */
	public Long countByIdNotAndName(Long id, String name);
}
