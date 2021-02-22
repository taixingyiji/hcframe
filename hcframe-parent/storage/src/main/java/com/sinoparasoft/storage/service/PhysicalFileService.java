package com.sinoparasoft.storage.service;

import com.sinoparasoft.storage.domain.LogicalFile;
import com.sinoparasoft.storage.domain.PhysicalFile;
import com.sinoparasoft.storage.domain.PhysicalTileFile;
import com.sinoparasoft.storage.model.Metadata;
import com.sinoparasoft.storage.repository.LogicalFileRepository;
import com.sinoparasoft.storage.repository.PhysicalFileRepository;
import com.sinoparasoft.storage.repository.PhysicalTileFileRepository;
import com.sinoparasoft.storage.utils.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物理文件管理服务类
 *
 * @author 袁涛
 * @date 2018.09.14
 */
@Component
public class PhysicalFileService {
	private static final Logger logger = LoggerFactory.getLogger(PhysicalFileService.class);

	/**
	 * 物理文件管理接口
	 */
	@Autowired
	private PhysicalFileRepository physicalFileRepository;

	/**
	 * 物理分块文件管理接口
	 */
	@Autowired
	private PhysicalTileFileRepository physicalTileFileRepository;

	@Autowired
	private PhysicalTileFileService physicalTileFileService;

	/**
	 * 逻辑文件管理接口
	 */
	@Autowired
	private LogicalFileRepository logicalFileRepository;

	/**
	 * 元数据管理服务类
	 */
	@Autowired
	private MetadataService metadataService;

	/**
	 * 新建物理文件
	 *
	 * @param logicalUuid
	 *            逻辑文件编号
	 * @param tile
	 *            文件块号，从0开始
	 * @param tiles
	 *            文件块数
	 * @param file
	 *            文件内容
	 */
	public void create(String logicalUuid, Integer tile, Integer tiles, MultipartFile file) {
		// 检查参数
		if (StringUtils.isBlank(logicalUuid)) {
			String message = "logical uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if(tile == null || tile < 0) {
			String message = "tile is null or less than 0.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if(tiles == null || tiles < 0) {
			String message = "tiles is null or less than 0.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if(file == null) {
			String message = "file is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 检查逻辑文件是否存在
		if (this.logicalFileRepository.countByUuid(logicalUuid) == 0) {
			String message = "find logical file by uuid failed, uuid = " + logicalUuid;
			logger.error(message);

			throw new RuntimeException(message);
		}
		LogicalFile logicalFile = this.logicalFileRepository.findFirstByUuid(logicalUuid);

		PhysicalFile physicalFile = null;
		if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) > 0) {
			physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
		} else {
			physicalFile = new PhysicalFile();
			physicalFile.setUuid(logicalFile.getPhysicalUuid());
			// 物理文件块数
			physicalFile.setTiles(tiles);
			// 创建时间
			physicalFile.setCreateTime(new Date());
			// 修改时间
			physicalFile.setModifiedTime(new Date());
			// 校验文件是否健康
			physicalFile.setHealthy(false);
			// 校验时间
			physicalFile.setCheckTime(new Date());
			// 保存物理文件信息
			this.physicalFileRepository.save(physicalFile);
		}
		// 存储物理分块文件
		this.physicalTileFileService.create(physicalFile.getUuid(), tile, file);
		if (this.physicalTileFileRepository.countByPhysicalUuid(physicalFile.getUuid()).intValue() == tiles) {
			// 物理分块文件存储完毕
			List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
					.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
			List<String> filePathList = new ArrayList<String>();
			Long length = 0L;
			for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
				filePathList.add(physicalTileFile.getPath());
				length += physicalTileFile.getLength();
			}
			// 文件MD5
			String md5 = DigestUtils.calculate(filePathList, DigestUtils.MD5);
			physicalFile.setMd5(md5);
			// 文件SHA
			String sha = DigestUtils.calculate(filePathList, DigestUtils.SHA);
			physicalFile.setSha(sha);
			// 查询物理文件是否存在
			if (this.physicalFileRepository.countByMd5AndSha(md5, sha) > 0) {
				// 物理文件已经存在
				PhysicalFile srcPhysicalFile = this.physicalFileRepository.findFirstByMd5AndSha(md5, sha);
				// 删除物理文件记录
				this.delete(physicalFile.getUuid());

				// 保存逻辑文件信息
				logicalFile.setPhysicalUuid(srcPhysicalFile.getUuid());
				// 文件长度
				logicalFile.setLength(srcPhysicalFile.getLength());
				// 元数据，JSON格式
				Metadata metadata = new Metadata();
				metadata.setName(logicalFile.getName());
				metadata.setLength(logicalFile.getLength());
				metadata.setModifiedTime(logicalFile.getModifiedTime());
				metadata.setCreateTime(logicalFile.getCreateTime());
				metadata.setTag(logicalFile.getTag());
				logicalFile.setMetadata(this.metadataService.toJson(metadata));
				this.logicalFileRepository.save(logicalFile);

			} else {
				// 物理文件不存在
				// 修改时间
				physicalFile.setModifiedTime(new Date());
				// 文件长度
				physicalFile.setLength(length);
				// 校验文件是否健康
				physicalFile.setHealthy(true);
				// 校验时间
				physicalFile.setCheckTime(new Date());
				// 保存物理文件信息
				this.physicalFileRepository.save(physicalFile);

				// 保存逻辑文件信息
				logicalFile.setLength(length);
				// 元数据，JSON格式
				Metadata metadata = new Metadata();
				metadata.setName(logicalFile.getName());
				metadata.setLength(logicalFile.getLength());
				metadata.setModifiedTime(logicalFile.getModifiedTime());
				metadata.setCreateTime(logicalFile.getCreateTime());
				metadata.setTag(logicalFile.getTag());
				logicalFile.setMetadata(this.metadataService.toJson(metadata));
				this.logicalFileRepository.save(logicalFile);
			}
		}
	}

	/**
	 * 删除物理文件
	 *
	 * @param uuid
	 *            物理文件编号
	 * @return 是否成功
	 */
	public Boolean delete(String uuid) {
		Boolean retValue = false;

		if (this.physicalFileRepository.countByUuid(uuid) > 0) {
			PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(uuid);
			if (this.physicalTileFileRepository.countByPhysicalUuid(physicalFile.getUuid()) > 0) {
				// 删除物理分块文件记录
				List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
						.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
				this.physicalTileFileRepository.deleteAll(physicalTileFileList);
			}
			// 删除物理文件记录
			this.physicalFileRepository.delete(physicalFile);
			retValue = true;
		}

		return retValue;
	}

}
