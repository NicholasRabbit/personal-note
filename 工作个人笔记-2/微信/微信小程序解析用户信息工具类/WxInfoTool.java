package com.jeesite.modules.common.util;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.modules.weixin.entity.RecycleSeller;
import com.jeesite.modules.weixin.service.RecycleSellerService;
import javafx.beans.binding.ObjectExpression;
import org.apache.http.HttpEntity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    //获取当前用户的access_token，以此来获取用户的手机号等关键信息
    public String getAccessToken(String appid,String secret){
        String infoUrl02 = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ appid +"&secret=" + secret;
        String userStr = HttpClientUtils.get(infoUrl02);
        JSONObject userInfo = JSONObject.parseObject(userStr);
        return userInfo.getString("access_token");
    }

	//解密手机号,头像，昵称，性别等信息
	public JSONObject getUserInfo(String session_key, String encryptedData, String iv){
		JSONObject userInfo = WxDecodeUtil.getPhoneNumber(sessionKey, encryptedData, iv);
		return  userInfo;
	}
	



    //获取小程序二维码
    public String qrCodeUrl(String accessToken, String baseDir,String parentId){
        String url="https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        //String json = "{\"scene\":\""+parentId+"\",\"width\":300,\"is_hyaline\":true}";
        Map<String, Object> map = new HashMap<>();
        map.put("scene",parentId);
        map.put("page","user/bind-user/index");
        map.put("width",300);
        map.put("is_hyaline",true);
        map.put("check_path",false);
        byte[] buffer = HttpPostUtil.post(url,map);
        String  qrCodePath = "/userfiles/weixin/feiluAppCode.png";
        File file = new File(baseDir + qrCodePath);
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(buffer);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return qrCodePath;
    }


}
