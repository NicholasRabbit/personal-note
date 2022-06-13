package com.jeesite.modules.flyl.web;

import java.util.List;
import java.util.Map;

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
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.idgen.IdGen;
import com.jeesite.modules.sys.utils.UserUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.flyl.entity.FlylArticleCate;
import com.jeesite.modules.flyl.service.FlylArticleCateService;

/**
 * 文章分类Controller
 * @author sxh
 * @version 2022-05-18
 */
@Controller
@RequestMapping(value = "${adminPath}/flyl/flylArticleCate")
public class FlylArticleCateController extends BaseController {

	@Autowired
	private FlylArticleCateService flylArticleCateService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public FlylArticleCate get(String cateCode, boolean isNewRecord) {
		return flylArticleCateService.get(cateCode, isNewRecord);
	}
	
	/**
	 * 管理主页
	 */
	@RequiresPermissions("flyl:flylArticleCate:view")
	@RequestMapping(value = "index")
	public String index(FlylArticleCate flylArticleCate, Model model) {
		model.addAttribute("flylArticleCate", flylArticleCate);
		return "modules/flyl/flylArticleCateIndex";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("flyl:flylArticleCate:view")
	@RequestMapping(value = {"list", ""})
	public String list(FlylArticleCate flylArticleCate, Model model) {
		model.addAttribute("flylArticleCate", flylArticleCate);
		return "modules/flyl/flylArticleCateList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public List<FlylArticleCate> listData(FlylArticleCate flylArticleCate) {
		if (StringUtils.isBlank(flylArticleCate.getParentCode())) {
			flylArticleCate.setParentCode(FlylArticleCate.ROOT_CODE);
		}
		if (StringUtils.isNotBlank(flylArticleCate.getCateCode())){
			flylArticleCate.setParentCode(null);
		}
		if (StringUtils.isNotBlank(flylArticleCate.getCateName())){
			flylArticleCate.setParentCode(null);
		}
		if (StringUtils.isNotBlank(flylArticleCate.getCateImg())){
			flylArticleCate.setParentCode(null);
		}
		List<FlylArticleCate> list = flylArticleCateService.findList(flylArticleCate);
		return list;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("flyl:flylArticleCate:view")
	@RequestMapping(value = "form")
	public String form(FlylArticleCate flylArticleCate, Model model) {
		// 创建并初始化下一个节点信息
		flylArticleCate = createNextNode(flylArticleCate);
		model.addAttribute("flylArticleCate", flylArticleCate);
		return "modules/flyl/flylArticleCateForm";
	}
	
	/**
	 * 创建并初始化下一个节点信息，如：排序号、默认值
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@RequestMapping(value = "createNextNode")
	@ResponseBody
	public FlylArticleCate createNextNode(FlylArticleCate flylArticleCate) {
		if (StringUtils.isNotBlank(flylArticleCate.getParentCode())){
			flylArticleCate.setParent(flylArticleCateService.get(flylArticleCate.getParentCode()));
		}
		if (flylArticleCate.getIsNewRecord()) {
			FlylArticleCate where = new FlylArticleCate();
			where.setParentCode(flylArticleCate.getParentCode());
			FlylArticleCate last = flylArticleCateService.getLastByParentCode(where);
			// 获取到下级最后一个节点
			if (last != null){
				flylArticleCate.setTreeSort(last.getTreeSort() + 30);
			}
		}
		// 以下设置表单默认数据
		if (flylArticleCate.getTreeSort() == null){
			flylArticleCate.setTreeSort(FlylArticleCate.DEFAULT_TREE_SORT);
		}
		return flylArticleCate;
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated FlylArticleCate flylArticleCate) {
		flylArticleCateService.save(flylArticleCate);
		return renderResult(Global.TRUE, text("保存文章分类成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(FlylArticleCate flylArticleCate) {
		FlylArticleCate where = new FlylArticleCate();
		where.setStatus(FlylArticleCate.STATUS_NORMAL);
		where.setParentCodes("," + flylArticleCate.getId() + ",");
		long count = flylArticleCateService.findCount(where);
		if (count > 0) {
			return renderResult(Global.FALSE, text("该文章分类包含未停用的子文章分类！"));
		}
		flylArticleCate.setStatus(FlylArticleCate.STATUS_DISABLE);
		flylArticleCateService.updateStatus(flylArticleCate);
		return renderResult(Global.TRUE, text("停用文章分类成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(FlylArticleCate flylArticleCate) {
		flylArticleCate.setStatus(FlylArticleCate.STATUS_NORMAL);
		flylArticleCateService.updateStatus(flylArticleCate);
		return renderResult(Global.TRUE, text("启用文章分类成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(FlylArticleCate flylArticleCate) {
		flylArticleCateService.delete(flylArticleCate);
		return renderResult(Global.TRUE, text("删除文章分类成功！"));
	}
	
	/**
	 * 获取树结构数据
	 * @param excludeCode 排除的Code
	 * @param parentCode 设置父级编码返回一级
	 * @param isShowCode 是否显示编码（true or 1：显示在左侧；2：显示在右侧；false or null：不显示）
	 * @return
	 */
	@RequiresPermissions("flyl:flylArticleCate:view")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData(String excludeCode, String parentCode, String isShowCode) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		FlylArticleCate where = new FlylArticleCate();
		where.setStatus(FlylArticleCate.STATUS_NORMAL);
		if (StringUtils.isNotBlank(parentCode)){
			where.setParentCode(parentCode);
		}
		List<FlylArticleCate> list = flylArticleCateService.findList(where);
		for (int i=0; i<list.size(); i++){
			FlylArticleCate e = list.get(i);
			// 过滤非正常的数据
			if (!FlylArticleCate.STATUS_NORMAL.equals(e.getStatus())){
				continue;
			}
			// 过滤被排除的编码（包括所有子级）
			if (StringUtils.isNotBlank(excludeCode)){
				if (e.getId().equals(excludeCode)){
					continue;
				}
				if (e.getParentCodes().contains("," + excludeCode + ",")){
					continue;
				}
			}
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentCode());
			map.put("name", StringUtils.getTreeNodeName(isShowCode, e.getCateCode(), e.getCateName()));
			map.put("isParent", !e.getIsTreeLeaf());
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 修复表结构相关数据
	 */
	@RequiresPermissions("flyl:flylArticleCate:edit")
	@RequestMapping(value = "fixTreeData")
	@ResponseBody
	public String fixTreeData(FlylArticleCate flylArticleCate){
		if (!UserUtils.getUser().isAdmin()){
			return renderResult(Global.FALSE, "操作失败，只有管理员才能进行修复！");
		}
		flylArticleCateService.fixTreeData();
		return renderResult(Global.TRUE, "数据修复成功");
	}
	
}