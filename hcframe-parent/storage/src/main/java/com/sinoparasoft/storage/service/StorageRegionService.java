package com.sinoparasoft.storage.service;

import com.sinoparasoft.storage.domain.StorageRegion;
import com.sinoparasoft.storage.model.StorageRegionQueryRequest;
import com.sinoparasoft.storage.repository.PhysicalTileFileRepository;
import com.sinoparasoft.storage.repository.StorageRegionRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 物理存储区管理服务类
 *
 * @author 袁涛
 * @date 2017.10.27
 */
@Component
public class StorageRegionService {
	private static final Logger logger = LoggerFactory.getLogger(StorageRegionService.class);

	/**
	 * 存储区管理接口
	 */
	@Autowired
	private StorageRegionRepository storageRegionRepository;

	/**
	 * 物理分块文件管理接口
	 */
	@Autowired
	private PhysicalTileFileRepository physicalTileFileRepository;

	/**
	 * 获取物理存储区列表
	 *
	 * @param request
	 *            物理存储区查询请求
	 * @param pageable
	 *            分页信息
	 * @return 物理存储区列表
	 */
	public Page<StorageRegion> findAll(StorageRegionQueryRequest request, Pageable pageable) {
		if (StringUtils.isBlank(request.getName())) {
			// 物理存储区名称为空
			return this.storageRegionRepository.findAll(pageable);
		} else {
			// 物理存储区名称不为空
			String name = request.getName();
			if (!name.startsWith("%")) {
				name = "%" + name;
			}
			if (!name.endsWith("%")) {
				name = name + "%";
			}
			return this.storageRegionRepository.findByNameLike(name, pageable);
		}
	}

	/**
	 * 根据物理存储区编号查询物理存储区信息
	 *
	 * @param id
	 *            物理存储区编号
	 * @return 物理存储区信息
	 */
	public StorageRegion findById(Long id) {
		StorageRegion retValue = null;
		if (id == null) {
			String message = "storage region id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		Optional<StorageRegion> query = this.storageRegionRepository.findById(id);
		if (query.isPresent()) {
			retValue = query.get();
		} else {
			String message = "find storage region by id failed, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
		return retValue;
	}

	/**
	 * 新建物理存储区
	 *
	 * @param request
	 *            物理存储区信息
	 * @return 物理存储区信息
	 */
	public StorageRegion create(StorageRegion request) {
		// 检查参数
		if (StringUtils.isBlank(request.getName())) {
			String message = "storage region name is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(request.getPath())) {
			String message = "storage region path is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 检查存储区是否存在
		if (this.storageRegionRepository.countByName(request.getName()) > 0) {
			String message = "create storage region failed, name already exists, name = " + request.getName();
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 写入数据库
		StorageRegion region = new StorageRegion();
		// 存储区编号
		String uuid = UUID.randomUUID().toString();
		region.setUuid(uuid);
		// 存储区名称
		region.setName(request.getName());
		// 存储区描述
		region.setDescription(request.getDescription());
		// 存储区路径
		region.setPath(request.getPath());
		// 存储区可用容量，单位：字节
		File file = new File(request.getPath());
		Long availableSize = file.getUsableSpace();
		region.setAvailableSize(availableSize);
		// 存储区已用容量，单位：字节
		Long usedSize = this.getDirectorySize(file);
		region.setUsedSize(usedSize);
		// 存储区总容量，单位：字节
		Long totalSize = availableSize + usedSize;
		region.setTotalSize(totalSize);
		// 检查存储区是否健康
		Boolean healthy = this.isHealthy(request.getPath());
		region.setHealthy(healthy);
		// 创建时间
		region.setCreateTime(new Date());
		// 修改时间
		region.setModifiedTime(new Date());
		// 检查时间
		region.setCheckTime(new Date());
		this.storageRegionRepository.save(region);

		String message = "create storage region sucess, name = " + request.getName();
		logger.info(message);

		return region;
	}

	/**
	 * 删除物理存储区
	 *
	 * @param id
	 *            物理存储区编号
	 */
	public void delete(Long id) {
		// 检查参数
		if (id == null) {
			String message = "delete storage region failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找物理存储区
		Optional<StorageRegion> query = this.storageRegionRepository.findById(id);
		if (query.isPresent()) {
			// 物理存储区存在，在数据库中删除记录
			StorageRegion region = query.get();
			// 查看物理存储区下是否存在文件
			if (this.physicalTileFileRepository.countByStorageRegionUuid(region.getUuid()) > 0) {
				// 存储区上有文件，不能删除，删除必须先清空存储区上的文件
				String message = "storage region has files, delete must clear files first, storage region uuid = "
						+ region.getUuid();
				logger.error(message);

				throw new RuntimeException(message);
			} else {
				this.storageRegionRepository.delete(region);
				String message = "delete storage region sucess, storage region id = " + id;
				logger.info(message);
			}
		} else {
			// 物理存储区不存在
			String message = "delete storage region failed, storage region does not exist, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 根据物理存储区UUID编号删除物理存储区
	 *
	 * @param uuid
	 *            物理存储区UUID编号
	 */
	public void delete(String uuid) {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "delete storage region failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找物理存储区
		if (this.storageRegionRepository.countByUuid(uuid) > 0) {
			// 查看物理存储区下是否存在文件
			if (this.physicalTileFileRepository.countByStorageRegionUuid(uuid) > 0) {
				// 存储区上有文件，不能删除，删除必须先清空存储区上的文件
				String message = "storage region has files, delete must clear files first, storage region uuid = "
						+ uuid;
				logger.error(message);

				throw new RuntimeException(message);
			} else {
				StorageRegion region = this.storageRegionRepository.findFirstByUuid(uuid);
				this.storageRegionRepository.delete(region);
				String message = "delete storage region sucess, storage region uuid = " + uuid;
				logger.info(message);
			}
		} else {
			// 物理存储区不存在
			String message = "delete storage region failed, storage region does not exist, uuid = " + uuid;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 批量删除物理存储区
	 *
	 * @param uuids
	 *            物理存储区UUID编号列表
	 */
	public void delete(List<String> uuids) {
		if (uuids != null && uuids.size() > 0) {
			for (String uuid : uuids) {
				this.delete(uuid);
			}
		}
	}

	/**
	 * 更新物理存储区
	 *
	 * @param request
	 *            物理存储区信息
	 */
	public void update(StorageRegion request) {
		// 检查参数
		if (request.getId() == null) {
			String message = "update storage region failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找物理存储区
		Optional<StorageRegion> query = this.storageRegionRepository.findById(request.getId());
		if (query.isPresent()) {
			// 物理存储区存在
			StorageRegion region = query.get();
			// 字段非空才更新
			if (!StringUtils.isBlank(request.getName())) {
				// 查询物理存储区是否重名
				if (this.storageRegionRepository.countByIdNotAndName(request.getId(), request.getName()) > 0) {
					String message = "update storage region failed, name already exists, name = " + request.getName();
					logger.error(message);
					throw new RuntimeException(message);
				} else {
					// 物理存储区名称
					region.setName(request.getName());
				}
			}
			if (!StringUtils.isBlank(request.getDescription())) {
				// 物理存储区描述
				region.setDescription(request.getDescription());
			}
			// 修改时间
			region.setModifiedTime(new Date());
			this.storageRegionRepository.save(region);
			String message = "update storage region information sucess, application name = " + request.getName();
			logger.info(message);
		} else {
			// 物理存储区不存在
			String message = "update storage region failed, application does not exist, id = " + request.getId();
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 获取可用存储区
	 *
	 * @param availableSize
	 *            可用空间
	 * @return 可用存储区
	 */
	public StorageRegion getAvailableStorageRegion(Long availableSize) {
		StorageRegion retValue = null;

		if (this.storageRegionRepository.countByAvailableSizeGreaterThanAndHealthy(availableSize, true) > 0) {
			List<StorageRegion> regions = this.storageRegionRepository
					.findByAvailableSizeGreaterThanAndHealthy(availableSize, true);
			retValue = regions.get(0);
		}

		return retValue;
	}

	/**
	 * 获取文件夹大小
	 *
	 * @param directory
	 *            文件夹
	 * @return 文件夹大小
	 */
	public Long getDirectorySize(File directory) {
		Long retValue = 0L;

		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				retValue += this.getDirectorySize(file);
			}
		} else {
			retValue = directory.length();
		}

		return retValue;
	}

	/**
	 * 判断文件夹是否健康
	 *
	 * @param path
	 *            文件夹路径
	 * @return 是否健康
	 */
	public Boolean isHealthy(String path) {
		Boolean retValue = false;

		String content = "test";
		String uuid = UUID.randomUUID().toString();
		Path filePath = Paths.get(path, uuid);
		File file = filePath.toFile();

		try {
			if (file.exists() || file.createNewFile()) {
				// 写入文件内容
				Files.write(filePath, content.getBytes());
				// 读取文件内容
				byte[] data = Files.readAllBytes(filePath);
				String text = new String(data);
				if (text.equals(content)) {
					// 删除文件
					Files.delete(filePath);
					retValue = true;
				} else {
					String msg = "write file content = " + content;
					msg += ", not eques to read file content = " + text;
					logger.error(msg);
				}
			} else {
				String msg = "create new file failed, path = " + filePath.toString();
				logger.error(msg);
			}

		} catch (FileNotFoundException e) {
			String msg = "find file failed, file path = " + filePath.toString();
			logger.error(msg, e);
		} catch (IOException e) {
			String msg = "read or write file failed, file path = " + filePath.toString();
			logger.error(msg, e);
		}

		return retValue;
	}

	/**
	 * 定期检查存储区状态，每隔一小时检查一次
	 */
	@Scheduled(cron = "30 * * * * ? ")
	public void check() {
		Long start = System.currentTimeMillis();
		if (this.storageRegionRepository.count() > 0) {
			List<StorageRegion> regions = this.storageRegionRepository.findAll();
			for (StorageRegion region : regions) {
				// 存储区可用容量，单位：字节
				File file = new File(region.getPath());
				Long availableSize = file.getUsableSpace();
				region.setAvailableSize(availableSize);
				// 存储区已用容量，单位：字节
				Long usedSize = this.getDirectorySize(file);
				region.setUsedSize(usedSize);
				// 存储区总容量，单位：字节
				Long totalSize = availableSize + usedSize;
				region.setTotalSize(totalSize);
				// 检查存储区是否健康
				Boolean healthy = this.isHealthy(region.getPath());
				region.setHealthy(healthy);
				// 检查时间
				region.setCheckTime(new Date());
				this.storageRegionRepository.save(region);
			}
		}
		Long end = System.currentTimeMillis();
		String msg = "check storage region use " + (end - start) + " ms.";
		logger.info(msg);
	}

}
