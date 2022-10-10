package com.uk.cloud.weixin.common.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.uk.cloud.weixin.common.constant.WxMpInfo;
import com.uk.cloud.weixin.common.dto.WxMpTemplateMsgDto;

public class WxInfoTool {
    private static volatile WxInfoTool instance;

    String msgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private WxInfoTool(){

    }
    public static WxInfoTool getInstance(){
        if(instance == null){
            synchronized (WxInfoTool.class) {
                if(instance == null) {
                    instance = new WxInfoTool();
                }
            }
        }
        return instance;
    }

    //获取openid, session_key的方法
    public String getWechatInfo(String code,String appid,String secret){
        //向微信服务其发送请求，获取返回值openid，session_key
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        String s = HttpClientUtils.get(url);
        return s;
    }




    //获取用户手机号,新版
    /*private Object getPhoneInfo(String access_token){
        String phoneUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";  //替换ACCESS_TOKEN
        Map<String,String>  dataMap = new HashMap<>();
        dataMap.put("access_token", access_token);
        Object phoneInfo =  HttpClientUtils.post(phoneUrl,dataMap);
        return phoneInfo;
    }*/

    //获取当前用户的access_token，以此来获取用户的手机号等关键信息
    public String getAccessToken(String appid,String secret){
        String infoUrl02 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ appid +"&secret=" + secret;
        String userStr = HttpClientUtils.get(infoUrl02);
        JSONObject jsonObject = JSONUtil.parseObj(userStr);
        return jsonObject.getStr("access_token");
    }

    /**
     *公众号发送消息调用接口
     * http请求方式: POST https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
     */
    public String sendTemplateMsg(WxMpTemplateMsgDto wxMpTemplateMsgDto){
        String accessToken = getAccessToken(WxMpInfo.APPID, WxMpInfo.SECRET);
        JSON json = JSONUtil.parse(wxMpTemplateMsgDto);
        String jsonStr = json.toString();
        System.out.printf("jsStr==>" + jsonStr);
        Object phoneInfo =  HttpClientUtils.ajaxPostJson(msgUrl+accessToken,jsonStr);
        return JSONUtil.toJsonStr(phoneInfo);

    }

    /*private Map<String,String> initMap(WxMpTemplateMsg wxMpTemplateMsg){
        Map<String,String> map = new HashMap<>();
        map.put("touser","oBTBb5Ll9TsZX7-M4zjWY382VCGc");
        map.put("template_id",WxMpInfo.TEMPLATE_ID);
        map.put("url"," ");
        String jsonStr2 = JSONUtil.toJsonStr(wxMpTemplateMsg.getData());
        System.out.println("jsonStr2==>" + jsonStr2);
        map.put("data",jsonStr2);
        return map;

    }*/



}
