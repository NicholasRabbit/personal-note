

//ͬ����������
	@PostMapping(value="/syncOrderToWms")
	@ResponseBody
	public String syncOrderToWms(RecycleOrder recycleOrder){
		wmsAsyncService.transferOrderToWms(recycleOrder);
		return renderResult(Global.TRUE, text("ͬ���ɹ���"));
	}
