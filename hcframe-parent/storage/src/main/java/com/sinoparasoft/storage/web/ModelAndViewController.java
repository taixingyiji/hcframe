package com.sinoparasoft.storage.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * 模型视图控制器
 *
 * @author 袁涛
 *
 */
@CrossOrigin
@Controller
@RequestMapping("/")
public class ModelAndViewController {
	/**
	 * 获取首页面
	 *
	 * @return 首页面
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String index() {
		return "redirect:/files/index";
	}

	@RequestMapping(value = "/fragments/aside", method = RequestMethod.GET, produces = "text/html")
	public String aside() {
		return "fragments/aside";
	}

	@RequestMapping(value = "/fragments/hamburger", method = RequestMethod.GET, produces = "text/html")
	public String hamburger() {
		return "fragments/hamburger";
	}

	@RequestMapping(value = "/fragments/breadcrumb", method = RequestMethod.GET, produces = "text/html")
	public String breadcrumb() {
		return "fragments/breadcrumb";
	}

	@RequestMapping(value = "/views/layout", method = RequestMethod.GET, produces = "text/html")
	public String layout() {
		return "layout";
	}

	@RequestMapping(value = "/fragments/navbar", method = RequestMethod.GET, produces = "text/html")
	public String navbar() {
		return "fragments/navbar";
	}

	/**
	 * 获取物理存储区管理主页
	 *
	 * @return 物理存储区管理主页
	 */
	@RequestMapping(value = "/regions/index", method = RequestMethod.GET, produces = "text/html")
	public String storageRegionIndex(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "regions");
		return "regions/index";
	}

	/**
	 * 获取逻辑存储区管理主页
	 *
	 * @return 逻辑存储区管理主页
	 */
	@RequestMapping(value = "/buckets/index", method = RequestMethod.GET, produces = "text/html")
	public String bucketIndex(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "buckets");
		return "buckets/index";
	}

	/**
	 * 获取数据管理主页
	 *
	 * @return 数据管理主页
	 */
	@RequestMapping(value = "/files/index", method = RequestMethod.GET, produces = "text/html")
	public String fileIndex(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "files");
		return "files/index";
	}

	/**
	 * 获取文件上传页面
	 *
	 * @return 文件上传页面
	 */
	@RequestMapping(value = "/files/upload", method = RequestMethod.GET, produces = "text/html")
	public String fileUpload(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "files");
		return "files/upload";
	}

	/**
	 * 获取文件批量上传页面
	 *
	 * @return 文件批量上传页面
	 */
	@RequestMapping(value = "/files/plupload", method = RequestMethod.GET, produces = "text/html")
	public String filePlupload(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "files");
		return "files/plupload";
	}

	/**
	 * 获取文件上传页面
	 *
	 * @return 文件上传页面
	 */
	@RequestMapping(value = "/files/elupload", method = RequestMethod.GET, produces = "text/html")
	public String fileElupload(Model model) {
		model.addAttribute("level1menu", "datas");
		model.addAttribute("level2menu", "files");
		return "files/elupload";
	}

}
