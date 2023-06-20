package com.by4cloud.platform.scdd.util;

import cn.hutool.extra.spring.SpringUtil;
import com.by4cloud.platform.admin.api.feign.RemoteDictService;
import com.by4cloud.platform.admin.api.feign.RemoteUserService;
import com.by4cloud.platform.admin.api.utils.RedisCacheUmpsHelper;
import com.by4cloud.platform.common.core.constant.SecurityConstants;
import com.by4cloud.platform.common.core.util.R;
import com.by4cloud.platform.scdd.api.feign.CompInfoFeignService;
import com.by4cloud.platform.scdd.constant.CacheConstants;
import com.by4cloud.platform.scdd.entity.CompInformation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisScddHelper {

	private static volatile RedisScddHelper instance;

	public static RedisScddHelper getInstance() {
		if (instance == null){
			synchronized (RedisCacheUmpsHelper.class){
				if (instance==null){
					instance = new RedisScddHelper();
				}
			}
			instance = new RedisScddHelper();
		}
		return instance;
	}
	private final RemoteDictService remoteDictService;
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	private final RedisTemplate<String,Object> redisTemplate;

	private RedisScddHelper() {
		this.remoteDictService = SpringUtil.getBean(RemoteDictService.class);
		this.remoteUserService = SpringUtil.getBean(RemoteUserService.class);
		this.cacheManager = SpringUtil.getBean(CacheManager.class);
		this.redisTemplate = SpringUtil.getBean(RedisTemplate.class);
	}

	public CompInformation getCompInfoById(Long compId){
		if(compId == null)
			return new CompInformation();
		Cache compCache = cacheManager.getCache(CacheConstants.COMP_INFO);
		if(compCache != null && compCache.get(compId) != null){
			R<CompInformation> r = compCache.get(compId, R.class);
			if (r.getData()!=null){
				return r.getData();
			}
		}

		CompInfoFeignService compInfoFeignService = SpringUtil.getBean(CompInfoFeignService.class);
		R<CompInformation> result = compInfoFeignService.getCompInfoById(compId, SecurityConstants.FROM_IN);
		CompInformation compInformation = result.getData();
		if(compInformation != null){
			return compInformation;
		}
		return null;
	}

}




