

//同步单个订单
	@PostMapping(value="/syncOrderToWms")
	@ResponseBody
	public String syncOrderToWms(RecycleOrder recycleOrder){
		wmsAsyncService.transferOrderToWms(recycleOrder);
		return renderResult(Global.TRUE, text("同步成功！"));
	}
