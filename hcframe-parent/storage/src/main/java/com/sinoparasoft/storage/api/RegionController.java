package com.sinoparasoft.storage.api;

import com.sinoparasoft.storage.domain.StorageRegion;
import com.sinoparasoft.storage.model.StorageRegionQueryRequest;
import com.sinoparasoft.storage.service.StorageRegionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 物理存储区管理控制类
 *
 * @author 袁涛
 * @date 2017.12.27
 */
@CrossOrigin
@RestController
@RequestMapping("/regions")
public class RegionController {
	/**
	 * 存储区管理服务类
	 */
	@Autowired
	private StorageRegionService storageRegionService;

	@ApiOperation(value = "获取物理存储区列表", notes = "分页查询，获取物理存储区列表")
	@GetMapping()
	public Page<StorageRegion> findAll(StorageRegionQueryRequest request, Pageable pageable) {
		return this.storageRegionService.findAll(request, pageable);
	}

	@ApiOperation(value = "获取物理存储区信息", notes = "根据物理存储区编号获取物理存储区信息")
	@GetMapping("/{id:\\d+}")
	public StorageRegion findById(@ApiParam(value = "物理存储区编号") @PathVariable Long id) {
		return this.storageRegionService.findById(id);
	}

	@ApiOperation(value = "新建物理存储区", notes = "新建物理存储区")
	@PostMapping()
	public StorageRegion create(@RequestBody StorageRegion request) {
		return this.storageRegionService.create(request);
	}

	@ApiOperation(value = "删除物理存储区", notes = "删除物理存储区")
	@DeleteMapping("/{id:\\d+}")
	public void delete(@ApiParam(value = "物理存储区编号", required = true) @PathVariable Long id) {
		this.storageRegionService.delete(id);
	}

	@ApiOperation(value = "更新物理存储区", notes = "更新物理存储区")
	@PutMapping()
	public void update(@RequestBody StorageRegion request) {
		this.storageRegionService.update(request);
	}
}
