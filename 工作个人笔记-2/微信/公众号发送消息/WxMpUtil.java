public class WxMpUtil {


	//有两种发送方式：

	//第一种：使用wxtool相关类发送模板消息，可用
	public R sendTemplateMsgByTool(@RequestBody WxMpRequestDTO wxMpRequestDTO) {
		WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
		WxMpTemplateMessage.MiniProgram miniProgram  = new WxMpTemplateMessage.MiniProgram();
		miniProgram.setAppid(wxMpConfigProperties.getMiniappId());  //设置点击跳转天宇药急送小程序
		miniProgram.setPagePath(wxMpConfigProperties.getPagepath());
		templateMessage.setMiniProgram(miniProgram);
		templateMessage.setTemplateId(wxMpRequestDTO.getTemplateId());
		//添加发送信息
		List<TemplateData> dataList = wxMpRequestDTO.getDataList();
		List<WxMpTemplateData> data = new ArrayList<>();
		for(TemplateData td : dataList){
			data.add(new WxMpTemplateData(td.getName(), td.getValue(), "#FF00FF"));
		}
		templateMessage.setData(data);
		//循环向多个用户发送模板消息
		for(WxUser wxUser : wxMpRequestDTO.getWxUserList()){
			templateMessage.setToUser(wxUser.getOpenId());
			sendTemplateMsg2(templateMessage);
		}
		return R.ok();
	}


	private String sendTemplateMsg2(WxMpTemplateMessage templateMessage) {
		WxMpService wxMpService = WxMpConfiguration.getMpService(wxMpConfigProperties.getAppId());  //填入公众号的appid
		String msgId = "";
		try {
			msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return msgId;

	}




	//第二种：  //手写发送模板消息，可用
	public R sendTemplateMsg(WxMpTemplateMsgDto wxMpTemplateMsg){
		WxInfoTool wxInfoTool = WxInfoTool.getInstance();
		Map<String,Object> map = new HashMap<>();
		map.put("first",new TemplateData("Hello","#FF00FF"));
		map.put("keyword1",new TemplateData("测试","#FF00FF"));
		map.put("keyword2",new TemplateData("模板消息","#FF00FF"));
		map.put("remark",new TemplateData("！！！","#FF00FF"));
		wxMpTemplateMsg.setData(map);
		wxMpTemplateMsg.setUrl(" ");
		String msg = wxInfoTool.sendTemplateMsg(wxMpTemplateMsg);
		return R.ok(msg);
	}

}