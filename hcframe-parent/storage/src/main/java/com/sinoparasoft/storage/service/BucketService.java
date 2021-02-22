package com.sinoparasoft.storage.service;

import com.sinoparasoft.storage.domain.Bucket;
import com.sinoparasoft.storage.model.BucketQueryRequest;
import com.sinoparasoft.storage.repository.BucketRepository;
import com.sinoparasoft.storage.repository.LogicalFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 逻辑存储区管理服务类
 *
 * @author 袁涛
 * @date 2018.06.08
 */
@Component
public class BucketService {
	private static final Logger logger = LoggerFactory.getLogger(BucketService.class);

	/**
	 * 逻辑存储区管理接口
	 */
	@Autowired
	private BucketRepository bucketRepository;

	/**
	 * 逻辑文件管理接口
	 */
	@Autowired
	private LogicalFileRepository logicalFileRepository;

	/**
	 * 获取逻辑存储区列表
	 *
	 * @param request
	 *            逻辑存储区查询请求
	 * @param pageable
	 *            分页信息
	 * @return 逻辑存储区列表
	 */
	public Page<Bucket> findAll(BucketQueryRequest request, Pageable pageable) {
		if (StringUtils.isBlank(request.getName())) {
			// 逻辑存储区名称为空
			return this.bucketRepository.findAll(pageable);
		} else {
			// 逻辑存储区名称不为空
			String name = request.getName();
			if (!name.startsWith("%")) {
				name = "%" + name;
			}
			if (!name.endsWith("%")) {
				name = name + "%";
			}
			return this.bucketRepository.findByNameLike(name, pageable);
		}
	}

	/**
	 * 根据逻辑存储区编号查询逻辑存储区信息
	 *
	 * @param id
	 *            逻辑存储区编号
	 * @return 逻辑存储区信息
	 */
	public Bucket findById(Long id) {
		Bucket retValue = null;
		if (id == null) {
			String message = "bucket id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		Optional<Bucket> query = this.bucketRepository.findById(id);
		if (query.isPresent()) {
			retValue = query.get();
		} else {
			String message = "find bucket by id failed, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
		return retValue;
	}

	/**
	 * 新建逻辑存储区
	 *
	 * @param request
	 *            逻辑存储区信息
	 * @return 逻辑存储区信息
	 */
	public Bucket create(Bucket request) {
		// 检查参数
		if (StringUtils.isBlank(request.getName())) {
			String message = "bucket name is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getTotalSize() == null || request.getTotalSize() < 0) {
			String message = "bucket total size is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 检查逻辑存储区是否存在
		if (this.bucketRepository.countByName(request.getName()) > 0) {
			String message = "create bucket failed, name already exists, name = " + request.getName();
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 写入数据库
		Bucket bucket = new Bucket();
		// 逻辑存储区名称
		bucket.setName(request.getName());
		// 逻辑存储区描述
		bucket.setDescription(request.getDescription());
		// 创建时间
		bucket.setCreateTime(new Date());
		// 修改时间
		bucket.setModifiedTime(new Date());
		// 元数据
		// 容量，单位：字节
		bucket.setTotalSize(request.getTotalSize());
		// 已使用容量，单位：字节
		bucket.setUsedSize(0L);
		// 剩余容量，单位：字节
		bucket.setAvailableSize(request.getTotalSize());
		// 保存逻辑存储区信息
		this.bucketRepository.save(bucket);

		String message = "create bucket sucess, name = " + request.getName();
		logger.info(message);

		return bucket;
	}

	/**
	 * 删除逻辑存储区
	 *
	 * @param id
	 *            逻辑存储区编号
	 */
	public void delete(Long id) {
		// 检查参数
		if (id == null) {
			String message = "delete bucket failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑存储区
		Optional<Bucket> query = this.bucketRepository.findById(id);
		if (query.isPresent()) {
			// 逻辑存储区存在，在数据库中删除记录
			Bucket bucket = query.get();
			// 逻辑存储区上有文件，不能删除，要删除必须先清空逻辑存储区上的文件
			if (this.logicalFileRepository.countByBucketName(bucket.getName()) > 0) {
				String message = "bucket has files, delete must clear files first, bucket id = " + id;
				logger.error(message);
				throw new RuntimeException(message);
			} else {
				this.bucketRepository.delete(bucket);
				String message = "delete bucket sucess, bucket id = " + id;
				logger.info(message);
			}
		} else {
			// 逻辑存储区不存在
			String message = "delete bucket failed, bucket does not exist, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 批量删除逻辑存储区
	 *
	 * @param ids
	 *            逻辑存储区编号列表
	 */
	public void delete(List<Long> ids) {
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				this.delete(id);
			}
		}
	}

	/**
	 * 更新逻辑存储区
	 *
	 * @param request
	 *            逻辑存储区信息
	 */
	public void update(Bucket request) {
		// 检查参数
		if (request.getId() == null) {
			String message = "update bucket failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑存储区
		Optional<Bucket> query = this.bucketRepository.findById(request.getId());
		if (query.isPresent()) {
			// 逻辑存储区存在
			Bucket bucket = query.get();
			// 字段非空才更新
			if (!StringUtils.isBlank(request.getName())) {
				// 查询逻辑存储区是否重名
				if (this.bucketRepository.countByIdNotAndName(request.getId(), request.getName()) > 0) {
					String message = "update bucket failed, name already exists, name = " + request.getName();
					logger.error(message);
					throw new RuntimeException(message);
				} else {
					// 逻辑存储区名称
					bucket.setName(request.getName());
				}
			}
			if (!StringUtils.isBlank(request.getDescription())) {
				// 逻辑存储区描述
				bucket.setDescription(request.getDescription());
			}
			if (request.getTotalSize() != null && request.getTotalSize() > 0) {
				// 容量，单位：字节
				bucket.setTotalSize(request.getTotalSize());
			}
			// 修改时间
			bucket.setModifiedTime(new Date());
			this.bucketRepository.save(bucket);
			String message = "update bucket information sucess, bucket name = " + request.getName();
			logger.info(message);
		} else {
			// 逻辑存储区不存在
			String message = "update bucket failed, bucket does not exist, id = " + request.getId();
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 定期检查逻辑存储区容量使用情况，每隔一小时检查一次
	 */
	@Scheduled(cron = "0 0 0/1 * * ? ")
	public void check() {
		Long start = System.currentTimeMillis();
		if (this.bucketRepository.count() > 0) {
			List<Bucket> buckets = this.bucketRepository.findAll();
			if (buckets != null && buckets.size() > 0) {
				for (Bucket bucket : buckets) {
					// 逻辑存储区已用容量，单位：字节
					Long usedSize = this.logicalFileRepository.sumLengthByBucketName(bucket.getName());
					if (usedSize == null) {
						usedSize = 0L;
					}
					bucket.setUsedSize(usedSize);
					// 逻辑存储区可用容量，单位：字节
					Long availableSize = bucket.getTotalSize() - bucket.getUsedSize();
					bucket.setAvailableSize(availableSize);
					// 检查时间
					bucket.setModifiedTime(new Date());
					this.bucketRepository.save(bucket);
				}
			}
		}
		Long end = System.currentTimeMillis();
		String msg = "check bucket use " + (end - start) + " ms.";
		logger.info(msg);
	}
}
