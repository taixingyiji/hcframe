package com.sinoparasoft.storage.service;

import com.sinoparasoft.storage.domain.Bucket;
import com.sinoparasoft.storage.domain.LogicalFile;
import com.sinoparasoft.storage.domain.PhysicalFile;
import com.sinoparasoft.storage.domain.PhysicalTileFile;
import com.sinoparasoft.storage.model.FileBlockCreateRequest;
import com.sinoparasoft.storage.model.FileCreateRequest;
import com.sinoparasoft.storage.model.LogicalFileQueryRequest;
import com.sinoparasoft.storage.repository.BucketRepository;
import com.sinoparasoft.storage.repository.LogicalFileRepository;
import com.sinoparasoft.storage.repository.PhysicalFileRepository;
import com.sinoparasoft.storage.repository.PhysicalTileFileRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * 数据分析任务服务类
 *
 * @author 袁涛
 * @date 2017.10.16
 */
@Component
public class FileService {
	private static final Logger logger = LoggerFactory.getLogger(FileService.class);

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

	/**
	 * 逻辑文件管理接口
	 */
	@Autowired
	private LogicalFileRepository logicalFileRepository;

	/**
	 * 逻辑存储区管理接口
	 */
	@Autowired
	private BucketRepository bucketRepository;

	@Autowired
	private PhysicalFileService physicalFileService;

	/**
	 * 文件服务中间目录地址
	 */
	@Value("${spring.servlet.multipart.location}")
	private String location;

	/**
	 * 获取逻辑文件列表
	 *
	 * @param request
	 *            逻辑文件查询请求
	 * @param pageable
	 *            分页信息
	 * @return 逻辑文件列表
	 */
	public Page<LogicalFile> findAll(LogicalFileQueryRequest request, Pageable pageable) {
		if (StringUtils.isBlank(request.getName())) {
			// 应用系统名称为空
			return this.logicalFileRepository.findAll(pageable);
		} else {
			// 应用系统名称不为空
			String name = request.getName();
			if (!name.startsWith("%")) {
				name = "%" + name;
			}
			if (!name.endsWith("%")) {
				name = name + "%";
			}
			return this.logicalFileRepository.findByNameLike(name, pageable);
		}
	}

	/**
	 * 根据逻辑文件编号查询逻辑文件信息
	 *
	 * @param id
	 *            逻辑文件编号
	 * @return 逻辑文件信息
	 */
	public LogicalFile findById(Long id) {
		LogicalFile retValue = null;
		// 检查参数
		if (id == null) {
			String message = "logical file id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			retValue = query.get();
		} else {
			String message = "find logical file by id failed, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
		return retValue;
	}

	/**
	 * 根据逻辑文件编号查询逻辑文件信息
	 *
	 * @param uuid
	 *            逻辑文件编号
	 * @return 逻辑文件信息
	 */
	public LogicalFile findByUuid(String uuid) {
		LogicalFile retValue = null;
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "logical file uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (this.logicalFileRepository.countByUuid(uuid) > 0) {
			retValue = this.logicalFileRepository.findFirstByUuid(uuid);
		} else {
			String message = "find logical file failed by uuid, uuid = " + uuid;
			logger.error(message);
			throw new RuntimeException(message);
		}

		return retValue;
	}

	/**
	 * 根据文件名称获取文件类型
	 *
	 * @param fileName
	 *            文件名称
	 * @return 文件类型
	 */
	public String getFileType(String fileName) {
		String retValue = "UNKNOWN";

		if (fileName.contains(".") && fileName.endsWith(".") == false) {
			String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
			retValue = suffix.toUpperCase();
		}

		return retValue;
	}

	/**
	 * 新建逻辑文件
	 *
	 * @param request
	 *            新建逻辑文件请求
	 * @return 逻辑文件信息
	 */
	public LogicalFile create(FileBlockCreateRequest request) {
		// 检查参数
		if (StringUtils.isBlank(request.getBucketName())) {
			String message = "bucket name is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getFile() == null) {
			String message = "file is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(request.getUuid())) {
			String message = "file uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (StringUtils.isBlank(request.getName())) {
			String message = "file name is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getLength() == null || request.getLength() < 0) {
			String message = "file length is null or less than 0.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getChunk() == null || request.getChunk() < 0) {
			String message = "file chunk is null or less than 0.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getChunks() == null || request.getChunks() < 0) {
			String message = "file chunks is null or less than 0.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查询逻辑存储区信息
		if (this.bucketRepository.countByName(request.getBucketName()) == 0) {
			String message = "find bucket name failed, bucket name = " + request.getBucketName();
			logger.error(message);

			throw new RuntimeException(message);
		}
		// 检查逻辑存储区的可用空间
		Bucket bucket = this.bucketRepository.findFirstByName(request.getBucketName());
		if (bucket.getAvailableSize() < 0) {
			String message = "bucket available size is used up, need to extend bucket size, bucket name = "
					+ request.getBucketName();
			logger.error(message);

			throw new RuntimeException(message);
		}

		LogicalFile logicalFile = null;
		if (this.logicalFileRepository.countByUuid(request.getUuid()) > 0) {
			logicalFile = this.logicalFileRepository.findFirstByUuid(request.getUuid());
		} else {
			logicalFile = new LogicalFile();
			logicalFile.setUuid(request.getUuid());
			// 文件名称
			logicalFile.setName(request.getName());
			// 文件类型
			String type = this.getFileType(request.getName());
			logicalFile.setType(type);
			// 物理文件编号
			String physicalUuid = UUID.randomUUID().toString();
			logicalFile.setPhysicalUuid(physicalUuid);
			// 创建时间
			logicalFile.setCreateTime(new Date());
			// 修改时间
			logicalFile.setModifiedTime(new Date());
			// 文件长度，单位：字节
			logicalFile.setLength(request.getLength());
			// 逻辑存储区名称
			logicalFile.setBucketName(request.getBucketName());
			// 是否公开
			logicalFile.setOpen(false);
			// 保存逻辑文件信息
			this.logicalFileRepository.save(logicalFile);
		}

		// 创建物理文件
		this.physicalFileService.create(request.getUuid(), request.getChunk(), request.getChunks(), request.getFile());

		String message = "create logical file sucess, name = " + request.getName();
		logger.info(message);

		return logicalFile;
	}

	/**
	 * 新建逻辑文件
	 *
	 * @param request
	 *            逻辑文件信息
	 * @return 逻辑文件信息
	 */
	public LogicalFile create(FileCreateRequest request) {
		// 检查参数
		if (StringUtils.isBlank(request.getBucketName())) {
			String message = "bucket name is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (request.getFile() == null) {
			String message = "file is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 新建逻辑文件块请求
		FileBlockCreateRequest fileBlockCreateRequest = new FileBlockCreateRequest();
		// 逻辑存储区名称
		fileBlockCreateRequest.setBucketName(request.getBucketName());
		// 文件内容
		fileBlockCreateRequest.setFile(request.getFile());
		// 逻辑文件编号
		String uuid = UUID.randomUUID().toString();
		fileBlockCreateRequest.setUuid(uuid);
		// 逻辑文件名称
		fileBlockCreateRequest.setName(request.getFile().getOriginalFilename());
		// 逻辑文件长度
		fileBlockCreateRequest.setLength(request.getFile().getSize());
		// 逻辑文件分块序号
		fileBlockCreateRequest.setChunk(0);
		// 逻辑文件分块数量
		fileBlockCreateRequest.setChunks(1);

		return this.create(fileBlockCreateRequest);
	}

	/**
	 * 删除逻辑文件
	 *
	 * @param id
	 *            逻辑文件编号
	 */
	public void delete(Long id) {
		// 检查参数
		if (id == null) {
			String message = "delete logical file failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			// 逻辑文件存在，在数据库中删除记录
			LogicalFile file = query.get();
			this.logicalFileRepository.delete(file);
			String message = "delete logical file sucess, logical file id = " + id;
			logger.info(message);
		} else {
			// 逻辑文件不存在
			String message = "delete logical file failed, logical file does not exist, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 根据逻辑文件UUID编号删除逻辑文件
	 *
	 * @param uuid
	 *            逻辑文件UUID编号
	 */
	public void delete(String uuid) {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "delete logical file failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (this.logicalFileRepository.countByUuid(uuid) > 0) {
			LogicalFile file = this.logicalFileRepository.findFirstByUuid(uuid);
			this.logicalFileRepository.delete(file);
			String message = "delete logical file sucess, logical file uuid = " + uuid;
			logger.info(message);
		} else {
			String msg = "find logical file failed by uuid, uuid = " + uuid;
			logger.error(msg);
		}
	}

	/**
	 * 批量删除逻辑文件
	 *
	 * @param uuids
	 *            逻辑文件编号列表
	 * @return 是否成功
	 */
	public void delete(List<String> uuids) {
		if (uuids != null && uuids.size() > 0) {
			for (String uuid : uuids) {
				this.delete(uuid);
			}
		}
	}

	/**
	 * 更新逻辑文件
	 *
	 * @param request
	 *            逻辑文件信息
	 */
	public void update(LogicalFile request) {
		// 检查参数
		if (request.getId() == null) {
			String message = "update logical file failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(request.getId());
		if (query.isPresent()) {
			// 逻辑文件存在
			LogicalFile file = query.get();
			// 字段非空才更新
			if (!StringUtils.isBlank(request.getName())) {
				// 逻辑文件名称
				file.setName(request.getName());
			}
			if (!StringUtils.isBlank(request.getType())) {
				// 文件类型
				file.setType(request.getType());
			}
			if (!StringUtils.isBlank(request.getMetadata())) {
				// 元数据，JSON格式
				file.setMetadata(request.getMetadata());
			}
			if (!StringUtils.isBlank(request.getTag())) {
				// 文件标签，版本
				file.setTag(request.getTag());
			}
			if (!StringUtils.isBlank(request.getBucketName())) {
				// 逻辑存储区名称
				file.setBucketName(request.getBucketName());
			}
			// 修改时间
			file.setModifiedTime(new Date());
			this.logicalFileRepository.save(file);
			String message = "update logical file information sucess, logical file name = " + request.getName();
			logger.info(message);
		} else {
			// 逻辑文件不存在
			String message = "update logical file failed, logical file does not exist, id = " + request.getId();
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 下载逻辑文件
	 *
	 * @param id
	 *            逻辑文件编号
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<Resource> download(HttpServletRequest request, Long id) throws IOException {
		// 检查参数
		if (id == null) {
			String message = "download logical file failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			LogicalFile logicalFile = query.get();
			// 检查物理文件是否存在
			if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) == 0) {
				// 物理文件不存在
				String message = "download logical file failed, logical file id = " + id;
				message += ", find physical file by uuid failed, physical file uuid = " + logicalFile.getPhysicalUuid();
				logger.error(message);
				throw new RuntimeException(message);
			}
			PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
			// 创建协议头
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/octet-stream");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			// 根据不同浏览器进行处理
			String agent = request.getHeader("User-Agent");
			if (agent.toLowerCase().contains("firefox")) {
				headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
						+ Base64.getEncoder().encodeToString(logicalFile.getName().getBytes("utf-8")) + "?=");
			} else {
				headers.add("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(logicalFile.getName(), "utf-8"));
			}
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("charset", "utf-8");
			// 创建输出流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 输出文件
			List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
					.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
			for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
				Files.copy(Paths.get(physicalTileFile.getPath()), out);
			}
			Resource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

			return ResponseEntity.ok().headers(headers).contentLength(physicalFile.getLength())
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} else {
			// 逻辑文件不存在
			String message = "download logical file failed, logical file does not exist, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 下载逻辑文件
	 *
	 * @param uuid
	 *            逻辑文件UUID编号
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<Resource> download(HttpServletRequest request, String uuid) throws IOException {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "download logical file failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 检查逻辑文件是否存在
		if (this.logicalFileRepository.countByUuid(uuid) == 0) {
			// 逻辑文件不存在
			String message = "download logical file failed, logical file does not exist, uuid = " + uuid;
			logger.error(message);
			throw new RuntimeException(message);
		}
		LogicalFile logicalFile = this.logicalFileRepository.findFirstByUuid(uuid);
		// 检查物理文件是否存在
		if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) == 0) {
			// 物理文件不存在
			String message = "download logical file failed, logical file uuid = " + uuid;
			message += ", find physical file by uuid failed, physical file uuid = " + logicalFile.getPhysicalUuid();
			logger.error(message);
			throw new RuntimeException(message);
		}
		PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
		// 创建协议头
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/octet-stream");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		// 根据不同浏览器进行处理
		String agent = request.getHeader("User-Agent");
		if (agent.toLowerCase().contains("firefox")) {
			headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
					+ Base64.getEncoder().encodeToString(logicalFile.getName().getBytes("utf-8")) + "?=");
		} else {
			headers.add("Content-Disposition",
					"attachment;fileName=" + URLEncoder.encode(logicalFile.getName(), "utf-8"));
		}
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("charset", "utf-8");
		// 创建输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 输出文件
		List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
				.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
		for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
			Files.copy(Paths.get(physicalTileFile.getPath()), out);
		}
		Resource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

		return ResponseEntity.ok().headers(headers).contentLength(physicalFile.getLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	/**
	 * 浏览逻辑文件
	 *
	 * @param uuid
	 *            逻辑文件UUID编号
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<Resource> view(HttpServletRequest request, String uuid) throws IOException {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "view logical file failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 检查逻辑文件是否存在
		if (this.logicalFileRepository.countByUuid(uuid) == 0) {
			// 逻辑文件不存在
			String message = "view logical file failed, logical file does not exist, uuid = " + uuid;
			logger.error(message);
			throw new RuntimeException(message);
		}
		LogicalFile logicalFile = this.logicalFileRepository.findFirstByUuid(uuid);
		// 检查物理文件是否存在
		if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) == 0) {
			// 物理文件不存在
			String message = "view logical file failed, logical file uuid = " + uuid;
			message += ", find physical file by uuid failed, physical file uuid = " + logicalFile.getPhysicalUuid();
			logger.error(message);
			throw new RuntimeException(message);
		}
		PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
		// 创建协议头
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		// 根据不同浏览器进行处理
		String agent = request.getHeader("User-Agent");
		if (agent.toLowerCase().contains("firefox")) {
			headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
					+ Base64.getEncoder().encodeToString(logicalFile.getName().getBytes("utf-8")) + "?=");
		} else {
			headers.add("Content-Disposition",
					"attachment;fileName=" + URLEncoder.encode(logicalFile.getName(), "utf-8"));
		}
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("charset", "utf-8");
		// 创建输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 输出文件
		List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
				.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
		for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
			Files.copy(Paths.get(physicalTileFile.getPath()), out);
		}
		MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
		if (logicalFile.getType().equalsIgnoreCase("html")) {
			contentType = MediaType.TEXT_HTML;
		} else if (logicalFile.getType().equalsIgnoreCase("txt")) {
			contentType = MediaType.TEXT_PLAIN;
		} else if (logicalFile.getType().equalsIgnoreCase("xml")) {
			contentType = MediaType.TEXT_XML;
		} else if (logicalFile.getType().equalsIgnoreCase("gif")) {
			contentType = MediaType.IMAGE_GIF;
		} else if (logicalFile.getType().equalsIgnoreCase("jpg")) {
			contentType = MediaType.IMAGE_JPEG;
		} else if (logicalFile.getType().equalsIgnoreCase("png")) {
			contentType = MediaType.IMAGE_PNG;
		} else if (logicalFile.getType().equalsIgnoreCase("json")) {
			contentType = MediaType.APPLICATION_JSON;
		} else if (logicalFile.getType().equalsIgnoreCase("pdf")) {
			contentType = MediaType.APPLICATION_PDF;
		}
		Resource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

		return ResponseEntity.ok().headers(headers).contentLength(physicalFile.getLength()).contentType(contentType)
				.body(resource);
	}

	/**
	 * 浏览逻辑文件
	 *
	 * @param id
	 *            逻辑文件编号
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<Resource> view(HttpServletRequest request, Long id) throws IOException {
		// 检查参数
		if (id == null) {
			String message = "view logical file failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			LogicalFile logicalFile = query.get();
			// 检查物理文件是否存在
			if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) == 0) {
				// 物理文件不存在
				String message = "view logical file failed, logical file id = " + id;
				message += ", find physical file by uuid failed, physical file uuid = " + logicalFile.getPhysicalUuid();
				logger.error(message);
				throw new RuntimeException(message);
			}
			PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
			// 创建协议头
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			// 根据不同浏览器进行处理
			String agent = request.getHeader("User-Agent");
			if (agent.toLowerCase().contains("firefox")) {
				headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
						+ Base64.getEncoder().encodeToString(logicalFile.getName().getBytes("utf-8")) + "?=");
			} else {
				headers.add("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(logicalFile.getName(), "utf-8"));
			}
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("charset", "utf-8");
			// 创建输出流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 输出文件
			List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
					.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
			for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
				Files.copy(Paths.get(physicalTileFile.getPath()), out);
			}
			MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
			if (logicalFile.getType().equalsIgnoreCase("html")) {
				contentType = MediaType.TEXT_HTML;
			} else if (logicalFile.getType().equalsIgnoreCase("txt")) {
				contentType = MediaType.TEXT_PLAIN;
			} else if (logicalFile.getType().equalsIgnoreCase("xml")) {
				contentType = MediaType.TEXT_XML;
			} else if (logicalFile.getType().equalsIgnoreCase("gif")) {
				contentType = MediaType.IMAGE_GIF;
			} else if (logicalFile.getType().equalsIgnoreCase("jpg")) {
				contentType = MediaType.IMAGE_JPEG;
			} else if (logicalFile.getType().equalsIgnoreCase("png")) {
				contentType = MediaType.IMAGE_PNG;
			} else if (logicalFile.getType().equalsIgnoreCase("json")) {
				contentType = MediaType.APPLICATION_JSON;
			} else if (logicalFile.getType().equalsIgnoreCase("pdf")) {
				contentType = MediaType.APPLICATION_PDF;
			}
			Resource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

			return ResponseEntity.ok().headers(headers).contentLength(physicalFile.getLength()).contentType(contentType)
					.body(resource);
		} else {
			// 逻辑文件不存在
			String message = "view logical file failed, logical file does not exist, id = " + id;
			logger.error(message);
			throw new RuntimeException(message);
		}
	}

	/**
	 * 文件开放
	 *
	 * @param id
	 *            文件编号
	 * @return 文件开放响应
	 */
	public void setOpenById(Long id) {
		// 检查参数
		if (id == null) {
			String message = "set file open failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			// 逻辑文件存在
			LogicalFile file = query.get();
			file.setOpen(true);
			// 修改时间
			file.setModifiedTime(new Date());
			this.logicalFileRepository.save(file);
			String message = "set logical file open sucess, logical file name = " + file.getName();
			logger.info(message);
		} else {
			String message = "set logical file open failed, logical file does not exist, id = " + id;
			throw new RuntimeException(message);
		}
	}

	/**
	 * 关闭文件不让公开访问
	 *
	 * @param id
	 *            文件编号
	 * @return 关闭文件响应
	 */
	public void setCloseById(Long id) {
		// 检查参数
		if (id == null) {
			String message = "set file close failed, id is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		Optional<LogicalFile> query = this.logicalFileRepository.findById(id);
		if (query.isPresent()) {
			// 逻辑文件存在
			LogicalFile file = query.get();
			file.setOpen(false);
			// 修改时间
			file.setModifiedTime(new Date());
			this.logicalFileRepository.save(file);
			String message = "set logical file close sucess, logical file name = " + file.getName();
			logger.info(message);
		} else {
			String message = "set logical file close failed, logical file does not exist, id = " + id;
			throw new RuntimeException(message);
		}
	}

	/**
	 * 文件开放
	 *
	 * @param uuid
	 *            文件编号
	 * @return 文件开放响应
	 */
	public void setOpenByUuid(String uuid) {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "set logical file open failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		LogicalFile file = this.logicalFileRepository.findFirstByUuid(uuid);
		if (file != null) {
			// 逻辑文件存在
			file.setOpen(true);
			// 修改时间
			file.setModifiedTime(new Date());
			this.logicalFileRepository.save(file);
			String message = "set logical file open sucess, logical file name = " + file.getName();
			logger.info(message);
		} else {
			String message = "set logical file open failed, logical file does not exist, uuid = " + uuid;
			throw new RuntimeException(message);
		}
	}

	/**
	 * 关闭文件不让公开访问
	 *
	 * @param uuid
	 *            文件编号
	 * @return 关闭文件响应
	 */
	public void setCloseByUuid(String uuid) {
		// 检查参数
		if (StringUtils.isBlank(uuid)) {
			String message = "set logical file close failed, uuid is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		// 查找逻辑文件
		LogicalFile file = this.logicalFileRepository.findFirstByUuid(uuid);
		if (file != null) {
			// 逻辑文件存在
			file.setOpen(false);
			// 修改时间
			file.setModifiedTime(new Date());
			this.logicalFileRepository.save(file);
			String message = "set logical file close sucess, logical file name = " + file.getName();
			logger.info(message);
		} else {
			String message = "set logical file close failed, logical file does not exist, uuid = " + uuid;
			throw new RuntimeException(message);
		}
	}

	/**
	 * 文件批量下载
	 *
	 * @param name
	 *            下载包名称
	 * @param uuids
	 *            uuid列表
	 * @return 文件压缩包
	 */
	public ResponseEntity<Resource> pldownload(HttpServletRequest request, String name, List<String> uuids) {
		// 检查参数
		if (StringUtils.isBlank(name)) {
			String message = "download logical files in package failed, name is null, please set a name for download package.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		if (uuids.isEmpty()) {
			String message = "download logical files in package failed, uuids is null.";
			logger.error(message);
			throw new RuntimeException(message);
		}
		try {
			// 创建临时文件夹
			String downloadTempDirectoryPath = this.location.concat(File.separator)
					.concat(UUID.randomUUID().toString());
			File downloadTempDirectory = new File(downloadTempDirectoryPath);
			downloadTempDirectory.mkdirs();
			// 下载文件
			for (String uuid : uuids) {
				// 检查逻辑文件是否存在
				if (this.logicalFileRepository.countByUuid(uuid) == 0) {
					// 逻辑文件不存在
					String message = "download logical file failed, logical file does not exist, uuid = " + uuid;
					logger.error(message);
					throw new RuntimeException(message);
				}
				LogicalFile logicalFile = this.logicalFileRepository.findFirstByUuid(uuid);
				// 检查物理文件是否存在
				if (this.physicalFileRepository.countByUuid(logicalFile.getPhysicalUuid()) == 0) {
					// 物理文件不存在
					String message = "download logical file failed, logical file uuid = " + uuid;
					message += ", find physical file by uuid failed, physical file uuid = "
							+ logicalFile.getPhysicalUuid();
					logger.error(message);
					throw new RuntimeException(message);
				}
				PhysicalFile physicalFile = this.physicalFileRepository.findFirstByUuid(logicalFile.getPhysicalUuid());
				// 获取文件名称
				String filename = logicalFile.getName();
				FileOutputStream out = new FileOutputStream(downloadTempDirectoryPath + File.separator + filename);
				// 输出文件
				List<PhysicalTileFile> physicalTileFileList = this.physicalTileFileRepository
						.findByPhysicalUuidOrderByTile(physicalFile.getUuid());
				for (PhysicalTileFile physicalTileFile : physicalTileFileList) {
					Files.copy(Paths.get(physicalTileFile.getPath()), out);
				}
			}
			// 压缩文件夹
			Project project = new Project();
			Zip zip = new Zip();
			zip.setProject(project);
			File zipFile = new File(downloadTempDirectoryPath.concat(".zip"));
			zip.setDestFile(zipFile);
			FileSet fileSet = new FileSet();
			fileSet.setProject(project);
			fileSet.setDir(downloadTempDirectory);
			zip.addFileset(fileSet);
			zip.execute();
			// 删除临时文件夹及压缩文件
			FileUtils.deleteDirectory(downloadTempDirectory);
			// 创建协议头
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/octet-stream");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			// 根据不同浏览器进行处理
			String agent = request.getHeader("User-Agent");
			if (agent.toLowerCase().contains("firefox")) {
				headers.add("Content-Disposition", "attachment;fileName==?UTF-8?B?"
						+ Base64.getEncoder().encodeToString(name.concat(".zip").getBytes("utf-8")) + "?=");
			} else {
				headers.add("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(name.concat(".zip"), "utf-8"));
			}
			headers.add("Pragma", "no-cache");
			headers.add("Expires", "0");
			headers.add("charset", "utf-8");
			// 创建输出流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Files.copy(zipFile.toPath(), out);
			// 删除压缩文件
			// zipFile.delete();
			Resource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

			return ResponseEntity.ok().headers(headers).contentLength(zipFile.length())
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} catch (FileNotFoundException e) {
			String message = "download logical file failed, name = " + name;
			message += ", uuids = " + uuids.toString();
			logger.error(message, e);
			throw new RuntimeException(message);
		} catch (IOException e) {
			String message = "download logical file failed, name = " + name;
			message += ", uuids = " + uuids.toString();
			logger.error(message, e);
			throw new RuntimeException(message);
		}
	}
}
