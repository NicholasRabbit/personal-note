package com.jeesite.modules.flyl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.flyl.entity.FlylArticleCate;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.flyl.entity.FlylArticle;
import com.jeesite.modules.flyl.service.FlylArticleService;

import java.util.Date;

/**
 * 文章Controller
 * @author sxh
 * @version 2022-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/flyl/flylArticle")
public class FlylArticleController extends BaseController {

	@Autowired
	private FlylArticleService flylArticleService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public FlylArticle get(String id, boolean isNewRecord) {
		return flylArticleService.get(id, isNewRecord);
	}


	@RequiresPermissions("flyl:flylArticle:view")
	@RequestMapping(value = "index")
	public String index(FlylArticleCate flylArticleCate, Model model) {
		model.addAttribute("flylArticleCate", flylArticleCate);
		return "modules/flyl/flylArticleIndex";
	}


	/**
	 * 查询列表
	 */
	@RequiresPermissions("flyl:flylArticle:view")
	@RequestMapping(value = {"list", ""})
	public String list(FlylArticle flylArticle, Model model) {
		model.addAttribute("flylArticle", flylArticle);
		return "modules/flyl/flylArticleList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("flyl:flylArticle:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<FlylArticle> listData(FlylArticle flylArticle, HttpServletRequest request, HttpServletResponse response) {
		logger.info("cateId==>" + flylArticle.getCateId());
		flylArticle.setPage(new Page<>(request, response));
		Page<FlylArticle> page = flylArticleService.findPage(flylArticle);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("flyl:flylArticle:view")
	@RequestMapping(value = "form")
	public String form(FlylArticle flylArticle, Model model) {
		model.addAttribute("flylArticle", flylArticle);
		return "modules/flyl/flylArticleForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("flyl:flylArticle:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated FlylArticle flylArticle) {
		flylArticle.setCreateTime(new Date());
		flylArticleService.save(flylArticle);
		return renderResult(Global.TRUE, text("保存文章成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("flyl:flylArticle:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(FlylArticle flylArticle) {
		flylArticle.setStatus(FlylArticle.STATUS_DISABLE);
		flylArticleService.updateStatus(flylArticle);
		return renderResult(Global.TRUE, text("停用文章成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("flyl:flylArticle:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(FlylArticle flylArticle) {
		flylArticle.setStatus(FlylArticle.STATUS_NORMAL);
		flylArticleService.updateStatus(flylArticle);
		return renderResult(Global.TRUE, text("启用文章成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("flyl:flylArticle:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(FlylArticle flylArticle) {
		flylArticleService.delete(flylArticle);
		return renderResult(Global.TRUE, text("删除文章成功！"));
	}
	
}