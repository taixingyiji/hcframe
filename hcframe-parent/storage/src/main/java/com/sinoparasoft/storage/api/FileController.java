package com.sinoparasoft.storage.api;

import com.sinoparasoft.storage.domain.LogicalFile;
import com.sinoparasoft.storage.model.FileBlockCreateRequest;
import com.sinoparasoft.storage.model.FileCreateRequest;
import com.sinoparasoft.storage.model.LogicalFileQueryRequest;
import com.sinoparasoft.storage.service.FileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 文件管理控制类
 *
 * @author 袁涛
 * @date 2017.05.10
 */
@CrossOrigin
@RestController
@RequestMapping("/files")
public class FileController {
	/**
	 * 文件管理服务类
	 */
	@Autowired
	private FileService fileService;

	@ApiOperation(value = "获取文件列表", notes = "分页查询，获取文件列表")
	@GetMapping()
	public Page<LogicalFile> findAll(LogicalFileQueryRequest request, Pageable pageable) {
		return this.fileService.findAll(request, pageable);
	}

	@ApiOperation(value = "获取文件信息", notes = "根据文件编号获取文件信息")
	@GetMapping("/id/{id:\\d+}")
	public LogicalFile findById(@ApiParam(value = "文件编号") @PathVariable Long id) {
		return this.fileService.findById(id);
	}

	@ApiOperation(value = "获取文件信息", notes = "根据文件编号获取文件信息")
	@GetMapping("/uuid/{uuid}")
	public LogicalFile findByUuid(@ApiParam(value = "文件编号") @PathVariable String uuid) {
		return this.fileService.findByUuid(uuid);
	}

	@ApiOperation(value = "获取UUID编号", notes = "获取UUID编号")
	@GetMapping("/uuid")
	public String createUuid() {
		return UUID.randomUUID().toString();
	}

	@ApiOperation(value = "新建文件", notes = "新建文件")
	@PostMapping()
	public LogicalFile create(@RequestParam("bucket_name") String bucketName,
			@RequestParam("file") MultipartFile file) {
		FileCreateRequest request = new FileCreateRequest();
		request.setBucketName(bucketName);
		request.setFile(file);
		return this.fileService.create(request);
	}

	@ApiOperation(value = "新建文件分块", notes = "新建文件分块")
	@PostMapping("/block")
	public LogicalFile createBlock(@RequestBody FileBlockCreateRequest request) {
		return this.fileService.create(request);
	}

	@ApiOperation(value = "新建文件分块", notes = "新建文件分块")
	@PostMapping("/block2")
	public LogicalFile createBlock2(@RequestParam("bucket_name") String bucketName,
			@RequestParam("uuid") String uuid,
			@RequestParam("name") String name,
			@RequestParam("length") Long length,
			@RequestParam("chunk") Integer chunk,
			@RequestParam("chunks") Integer chunks,
			@RequestParam("file") MultipartFile file) {
		FileBlockCreateRequest request = new FileBlockCreateRequest();
		request.setBucketName(bucketName);
		request.setUuid(uuid);
		request.setName(name);
		request.setLength(length);
		request.setChunk(chunk);
		request.setChunks(chunks);
		request.setFile(file);
		return this.fileService.create(request);
	}

	@ApiOperation(value = "删除文件", notes = "删除文件")
	@DeleteMapping("/id/{id:\\d+}")
	public void delete(@ApiParam(value = "文件编号", required = true) @PathVariable Long id) {
		this.fileService.delete(id);
	}

	@ApiOperation(value = "删除文件", notes = "删除文件")
	@DeleteMapping("/uuid/{uuid}")
	public void delete(@ApiParam(value = "文件编号", required = true) @PathVariable String uuid) {
		this.fileService.delete(uuid);
	}

	@ApiOperation(value = "更新文件", notes = "更新文件")
	@PutMapping()
	public void update(@RequestBody LogicalFile request) {
		this.fileService.update(request);
	}

	@ApiOperation(value = "下载文件", notes = "根据文件编号下载文件")
	@GetMapping("/download/id/{id:\\d+}")
	public ResponseEntity<Resource> downloadById(HttpServletRequest request,
			@ApiParam(value = "文件编号") @PathVariable Long id) throws IOException {
		return this.fileService.download(request, id);
	}

	@ApiOperation(value = "下载文件", notes = "根据文件UUID编号下载文件")
	@GetMapping("/download/uuid/{uuid}")
	public ResponseEntity<Resource> downloadByUuid(HttpServletRequest request,
			@ApiParam(value = "文件编号") @PathVariable String uuid) throws IOException {
		return this.fileService.download(request, uuid);
	}

	@ApiOperation(value = "批量下载文件", notes = "根据文件UUID编号列表批量下载文件")
	@GetMapping("/pldownload")
	public ResponseEntity<Resource> pldownload(HttpServletRequest request, @RequestParam("name") String name,
			@RequestParam("uuids") String uuids) throws IOException {
		List<String> uuidList = new ArrayList<String>();
		if (uuids != null && !uuids.isEmpty()) {
			if (uuids.contains(",")) {
				String[] list = uuids.split(",");
				for (String uuid : list) {
					uuidList.add(uuid);
				}
			} else {
				uuidList.add(uuids);
			}
		}
		return this.fileService.pldownload(request, name, uuidList);
	}

	@ApiOperation(value = "浏览文件", notes = "根据文件编号浏览文件")
	@GetMapping("/view/id/{id:\\d+}")
	public ResponseEntity<Resource> viewById(HttpServletRequest request,
			@ApiParam(value = "文件编号") @PathVariable Long id) throws IOException {
		return this.fileService.view(request, id);
	}

	@ApiOperation(value = "浏览文件", notes = "根据文件UUID编号浏览文件")
	@GetMapping("/view/uuid/{uuid}")
	public ResponseEntity<Resource> viewByUuid(HttpServletRequest request,
			@ApiParam(value = "文件编号") @PathVariable String uuid) throws IOException {
		return this.fileService.view(request, uuid);
	}

	@ApiOperation(value = "开放文件让公开访问", notes = "根据文件编号,开放文件让公开访问")
	@PostMapping("/set/id/{id:\\d+}/open")
	public void setOpenById(@ApiParam(value = "文件编号", required = true) @PathVariable Long id) {
		this.fileService.setOpenById(id);
	}

	@ApiOperation(value = "关闭文件不让公开访问", notes = "根据文件编号,关闭文件不让公开访问")
	@PostMapping("/set/id/{id:\\d+}/close")
	public void setCloseById(@ApiParam(value = "文件编号", required = true) @PathVariable Long id) {
		this.fileService.setCloseById(id);
	}

	@ApiOperation(value = "开放文件让公开访问", notes = "根据文件UUID编号,开放文件让公开访问")
	@GetMapping("/set/uuid/{uuid}/open")
	public void setOpenByUuid(@ApiParam(value = "文件编号") @PathVariable String uuid) {
		this.fileService.setOpenByUuid(uuid);
	}

	@ApiOperation(value = "关闭文件不让公开访问", notes = "根据文件UUID编号,关闭文件不让公开访问")
	@GetMapping("/set/uuid/{uuid}/close")
	public void setCloseByUuid(@ApiParam(value = "文件编号") @PathVariable String uuid) {
		this.fileService.setCloseByUuid(uuid);
	}
}
