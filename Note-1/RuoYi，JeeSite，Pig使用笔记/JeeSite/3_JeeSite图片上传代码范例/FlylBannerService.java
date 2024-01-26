package com.jeesite.modules.flyl.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.flyl.entity.FlylBanner;
import com.jeesite.modules.flyl.dao.FlylBannerDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 移动端首页轮播图Service
 * @author sxh
 * @version 2022-04-24
 */
@Service
@Transactional(readOnly=true)
public class FlylBannerService extends CrudService<FlylBannerDao, FlylBanner> {
	
	
	
	/**
	 * 保存数据（插入或更新）
	 * @param flylBanner
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(FlylBanner flylBanner) {
		super.save(flylBanner);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(flylBanner, flylBanner.getId(), "flylBanner_image");
	}
	

	
}