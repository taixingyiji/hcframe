package com.sinoparasoft.storage.api;

import com.sinoparasoft.storage.domain.Bucket;
import com.sinoparasoft.storage.model.BucketQueryRequest;
import com.sinoparasoft.storage.service.BucketService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 逻辑存储区管理控制类
 *
 * @author 袁涛
 * @date 2018.06.08
 */
@CrossOrigin
@RestController
@RequestMapping("/buckets")
public class BucketController {
	/**
	 * 逻辑存储区管理服务类
	 */
	@Autowired
	private BucketService bucketService;

	@ApiOperation(value = "获取逻辑存储区列表", notes = "分页查询，获取逻辑存储区列表")
	@GetMapping()
	public Page<Bucket> findAll(BucketQueryRequest request, Pageable pageable) {
		return this.bucketService.findAll(request, pageable);
	}

	@ApiOperation(value = "获取逻辑存储区信息", notes = "根据逻辑存储区编号获取逻辑存储区信息")
	@GetMapping("/{id:\\d+}")
	public Bucket findById(@ApiParam(value = "逻辑存储区编号") @PathVariable Long id) {
		return this.bucketService.findById(id);
	}

	@ApiOperation(value = "新建逻辑存储区", notes = "新建逻辑存储区")
	@PostMapping()
	public Bucket create(@RequestBody Bucket request) {
		return this.bucketService.create(request);
	}

	@ApiOperation(value = "删除逻辑存储区", notes = "删除逻辑存储区")
	@DeleteMapping("/{id:\\d+}")
	public void delete(@ApiParam(value = "逻辑存储区编号", required = true) @PathVariable Long id) {
		this.bucketService.delete(id);
	}

	@ApiOperation(value = "更新逻辑存储区", notes = "更新逻辑存储区")
	@PutMapping()
	public void update(@RequestBody Bucket request) {
		this.bucketService.update(request);
	}
}
