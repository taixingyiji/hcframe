package com.sinoparasoft.storage.repository;

import com.sinoparasoft.storage.domain.Bucket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 逻辑存储区管理接口
 *
 * @author 袁涛
 * @date 2018.06.07
 */
public interface BucketRepository extends JpaRepository<Bucket, Long> {
	/**
	 * 根据逻辑存储区名称统计逻辑存储区的个数
	 *
	 * @param name
	 *            逻辑存储区名称
	 * @return 逻辑存储区的个数
	 */
	public Long countByName(String name);

	/**
	 * 根据逻辑存储区名称查询逻辑存储区的信息
	 *
	 * @param name
	 *            逻辑存储区名称
	 * @return 逻辑存储区信息
	 */
	public Bucket findFirstByName(String name);

	/**
	 * 根据逻辑存储区编号统计逻辑存储区的个数
	 *
	 * @param id
	 *            逻辑存储区编号
	 * @return 逻辑存储区的个数
	 */
	public Long countById(Long id);

	/**
	 * 根据逻辑存储区编号查询逻辑存储区的信息
	 *
	 * @param id
	 *            逻辑存储区编号
	 * @return 逻辑存储区信息
	 */
	public Bucket findFirstById(Long id);

	/**
	 * 根据逻辑存储区名称查询存储区列表
	 *
	 * @param name
	 *            逻辑存储区名称
	 * @param pageable
	 *            分页信息
	 * @return 逻辑存储区列表
	 */
	public Page<Bucket> findByNameLike(String name, Pageable pageable);

	/**
	 * 根据逻辑存储区编号和逻辑存储区名称统计逻辑存储区个数
	 *
	 * @param id
	 *            逻辑存储区编号
	 * @param name
	 *            逻辑存储区名称
	 * @return 逻辑存储区个数
	 */
	public Long countByIdNotAndName(Long id, String name);
}
