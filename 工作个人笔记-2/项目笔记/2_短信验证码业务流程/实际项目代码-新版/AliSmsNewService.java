package com.jeesite.modules.recycle.service;

import com.aliyun.dysmsapi20170525.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.tea.*;
import com.aliyun.dysmsapi20170525.*;
import com.aliyun.dysmsapi20170525.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;

import com.aliyun.teautil.*;

import java.util.Arrays;
import java.util.List;

@Service
public class AliSmsNewService {

    @Value("${aliyun.accessKeyId}")
    String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    String accessKeySecret;

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public SendSmsResponse  sendSms(String phoneNumber,String code)  {

        Client client = null;
        SendSmsResponse resp = null;
        try {
            client = AliSmsNewService.createClient(accessKeyId, accessKeySecret);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName("斑马爱分类")  //这里对应阿里的签名名称
                    .setTemplateCode("SMS_236880385")  //签名模板名称
                    .setPhoneNumbers(phoneNumber)
                    .setTemplateParam("{\"code\":\""+ code +"\"}");
            resp = client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  resp;

    }

}
