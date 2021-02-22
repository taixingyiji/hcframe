package com.sinoparasoft.storage.service;

import com.sinoparasoft.storage.domain.PhysicalTileFile;
import com.sinoparasoft.storage.domain.StorageRegion;
import com.sinoparasoft.storage.repository.PhysicalTileFileRepository;
import com.sinoparasoft.storage.repository.StorageRegionRepository;
import com.sinoparasoft.storage.utils.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 物理分块文件管理服务类
 *
 * @author 袁涛
 * @date 2018.09.14
 */
@Component
public class PhysicalTileFileService {
	private static final Logger logger = LoggerFactory.getLogger(PhysicalTileFileService.class);

	/**
	 * 物理分块文件管理接口
	 */
	@Autowired
	private PhysicalTileFileRepository physicalTileFileRepository;

	/**
	 * 存储区管理接口
	 */
	@Autowired
	private StorageRegionRepository storageRegionRepository;

	/**
	 * 存储区管理服务
	 */
	@Autowired
	private StorageRegionService storageRegionService;

	/**
	 * 新建物理分块文件
	 *
	 * @param physicalUuid
	 *            物理文件编号
	 * @param tile
	 *            物理分块文件序号
	 * @param file
	 *            物理分块文件内容
	 */
	public void create(String physicalUuid, Integer tile, MultipartFile file) {
		// 检查参数
		if (StringUtils.isBlank(physicalUuid)) {
			String message = "physical file uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 获取可用存储区
		StorageRegion region = this.storageRegionService.getAvailableStorageRegion(file.getSize());
		if (region == null) {
			String message = "find available storage region failed, file size = " + file.getSize();
			logger.error(message);

			throw new RuntimeException(message);
		}

		// 创建文件存储目录
		Calendar calendar = Calendar.getInstance();
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH);
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		Path directoryPath = Paths.get(region.getPath(), year.toString(), month.toString(), day.toString());
		File directory = directoryPath.toFile();
		if (directory.exists() || directory.mkdirs()) {
			// 文件编号
			String uuid = UUID.randomUUID().toString();
			File destFile = new File(directory.getAbsolutePath(), uuid);
			try {
				file.transferTo(destFile);
				// 文件MD5
				String md5 = DigestUtils.calculate(destFile.getAbsolutePath(), DigestUtils.MD5);
				// 文件SHA
				String sha = DigestUtils.calculate(destFile.getAbsolutePath(), DigestUtils.SHA);
				if (this.physicalTileFileRepository.countByMd5AndSha(md5, sha) == 0) {
					// 物理分块文件不存在
					PhysicalTileFile physicalTileFile = new PhysicalTileFile();
					// 文件编号
					physicalTileFile.setUuid(uuid);
					// 物理文件编号
					physicalTileFile.setPhysicalUuid(physicalUuid);
					// 物理分块文件序号
					physicalTileFile.setTile(tile);
					// 文件路径
					physicalTileFile.setPath(destFile.getAbsolutePath());
					// 文件MD5
					physicalTileFile.setMd5(md5);
					// 文件SHA
					physicalTileFile.setSha(sha);
					// 创建时间
					physicalTileFile.setCreateTime(new Date());
					// 修改时间
					physicalTileFile.setModifiedTime(new Date());
					// 文件大小
					physicalTileFile.setLength(file.getSize());
					// 存储区编号
					physicalTileFile.setStorageRegionUuid(region.getUuid());
					// 文件是否健康
					physicalTileFile.setHealthy(true);
					// 校验时间
					physicalTileFile.setCheckTime(new Date());
					this.physicalTileFileRepository.save(physicalTileFile);
				} else {
					// 物理分块文件存在
					// 删除临时文件
					destFile.delete();
					// 复制物理分块文件信息
					PhysicalTileFile srcPhysicalTileFile = this.physicalTileFileRepository.findFirstByMd5AndSha(md5,
							sha);
					PhysicalTileFile destPhysicalTileFile = new PhysicalTileFile();
					// 文件编号
					destPhysicalTileFile.setUuid(uuid);
					// 物理文件编号
					destPhysicalTileFile.setPhysicalUuid(physicalUuid);
					// 物理分块文件序号
					destPhysicalTileFile.setTile(tile);
					// 文件路径
					destPhysicalTileFile.setPath(srcPhysicalTileFile.getPath());
					// 文件MD5
					destPhysicalTileFile.setMd5(srcPhysicalTileFile.getMd5());
					// 文件SHA
					destPhysicalTileFile.setSha(srcPhysicalTileFile.getSha());
					// 创建时间
					destPhysicalTileFile.setCreateTime(new Date());
					// 修改时间
					destPhysicalTileFile.setModifiedTime(new Date());
					// 文件大小
					destPhysicalTileFile.setLength(srcPhysicalTileFile.getLength());
					// 存储区编号
					destPhysicalTileFile.setStorageRegionUuid(srcPhysicalTileFile.getStorageRegionUuid());
					// 文件是否健康
					destPhysicalTileFile.setHealthy(srcPhysicalTileFile.getHealthy());
					// 校验时间
					destPhysicalTileFile.setCheckTime(srcPhysicalTileFile.getCheckTime());
					this.physicalTileFileRepository.save(destPhysicalTileFile);
				}
			} catch (IllegalStateException e) {
				String message = "store file failed, path = " + destFile.getAbsolutePath();
				logger.error(message);

				throw new RuntimeException(message);
			} catch (IOException e) {
				String message = "store file failed, path = " + destFile.getAbsolutePath();
				logger.error(message);

				throw new RuntimeException(message);
			}
		} else {
			// 文件存储目录不存在且目录创建失败
			region.setHealthy(false);
			region.setCheckTime(calendar.getTime());
			this.storageRegionRepository.save(region);

			String message = "directory not exits and create directory failed, directory = "
					+ directory.getAbsolutePath();
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 定期检查物理文件的状态，每隔一小时检查一次
	 */
	@Scheduled(cron = "0 0 0/1 * * ? ")
	public void check() {
		Long start = System.currentTimeMillis();
		if (this.physicalTileFileRepository.count() > 0) {
			List<PhysicalTileFile> files = this.physicalTileFileRepository.findAll();
			for (PhysicalTileFile file : files) {
				// 判断文件是否存在
				if (new File(file.getPath()).exists()) {
					// 文件MD5
					String md5 = DigestUtils.calculate(file.getPath(), DigestUtils.MD5);
					// 文件SHA
					String sha = DigestUtils.calculate(file.getPath(), DigestUtils.SHA);
					if (md5.equals(file.getMd5()) && sha.equals(file.getSha())) {
						// 文件是否健康
						file.setHealthy(true);
					} else {
						// 文件是否健康
						file.setHealthy(false);
					}
				} else {
					// 文件是否健康
					file.setHealthy(false);
				}

				// 校验时间
				file.setCheckTime(new Date());
				this.physicalTileFileRepository.save(file);
			}
		}
		Long end = System.currentTimeMillis();
		String msg = "check physical tile file use " + (end - start) + " ms.";
		logger.info(msg);
	}
}
