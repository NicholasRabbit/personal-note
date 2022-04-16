package com.jeesite.modules.util;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.util.JsonResult;
import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.modules.common.utill.UUIDUtil;
import com.jeesite.modules.recycle.entity.RecycleSeller;
import com.jeesite.modules.recycle.service.RecycleSellerService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class WxInfoTool {
    private static volatile WxInfoTool instance;
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

    //判断是否有当前用户的openid
    public RecycleSeller hasOpenid(RecycleSellerService recycleSellerService, String openid, String phoneNumber){

        RecycleSeller s = new RecycleSeller();
        if (openid != null && openid != ""){
            s.setWxOpenid(openid);
            s.setPhoneNumber(phoneNumber);
            RecycleSeller seller = recycleSellerService.getSellerByOpenid(s);  //通过openid向数据库查询该用户是否有数据，有则为登陆过的用户，无需再获取用户信息
            //如果没有openid，说明是新用户，则添加
            if(seller == null ){
                s.setId(UUIDUtil.fastUUId());
                s.setCreateTime(new Date());
                recycleSellerService.insert(s);
                return s;
            } else {
                if("".equals(seller.getPhoneNumber())  || seller.getPhoneNumber() == null){
                    seller.setPhoneNumber(phoneNumber);
                    recycleSellerService.update(seller);
                }

            }
            return  seller;
        }
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
    private String getAccessToken(String appid,String secret){
        String infoUrl02 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ appid +"&secret=" + secret;
        String userStr = HttpClientUtils.get(infoUrl02);
        JSONObject userInfo = JSONObject.parseObject(userStr);
        return userInfo.getString("access_token");
    }


}
