package com.ruoyi.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/*
 * 利用HttpClient进行post请求的工具类,键值对方式组装数据,发送 Https请求，需要配置SSLClient.java使用
 */
public class HttpsPostUtil {

    public static String doPost(String url, Map<String, String> map, String charset) {

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .build();
        CloseableHttpResponse response = null;
        CloseableHttpClient sslClient = null;
        String message = null;
        try {
            sslClient = new SSLClient();

            //设置发送的数据
            StringEntity s = new StringEntity(JSON.toJSONString(map));
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(s);
            response = sslClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {  //200表示请求成功
                HttpEntity entity = response.getEntity();
                message = EntityUtils.toString(entity, "utf-8");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sslClient.close();  //最后关闭资源，减少开销
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }


}
