

//在Controller层或Service层加注解，配合RedisHelper使用
	
	//注意这里是unless = "#result.data==null"，不能写unless = "#result==null"，因为要的是data内的数据，
	//有可能R不是null，但data是null。
	@Cacheable(cacheNames = CacheConstants.COMP_INFO,key = "#id", unless = "#result.data==null")  
	public R getById(@PathVariable("id" ) Long id) {
		return R.ok(compInformationService.getById(id));
	}


	@CacheEvict(cacheNames = CacheConstants.COMP_INFO,key = "#compInformation.id")
    public R updateById(@RequestBody CompInformation compInformation) {
		Map<String,String> itemMap = infoJsonToMap(compInformation.getInfoJson());
		if (itemMap.get("现核定能力（万t/a）") != null) {
			compInformation.setAllowProduction(Double.valueOf(itemMap.get("现核定能力（万t/a）")));
		}
        return R.ok(compInformationService.updateById(compInformation));
    }