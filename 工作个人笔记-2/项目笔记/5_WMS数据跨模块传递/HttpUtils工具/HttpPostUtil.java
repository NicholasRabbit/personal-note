package com.ruoyi.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

//跨模块发送Http请求的工具类
public class HttpPostUtil {


    public static String doPost(String url, Map<String, String> map, String charset) {

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .build();
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String message = null;
        try {
            httpClient = new DefaultHttpClient();

            //设置发送的数据
            //StringEntity s = new StringEntity(JSON.toJSONString(map));  //这种写法会导致中文乱码
			StringEntity s = new StringEntity(JSON.toJSONString(map),"UTF-8");  //设置编码格式防止中文乱码
            //s.setContentEncoding("UTF-8");  //这里就不用设置了
            s.setContentType("application/json");//发送json数据需要设置contentType
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(s);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {  //200表示请求成功
                HttpEntity entity = response.getEntity();
                message = EntityUtils.toString(entity, "utf-8");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();  //最后关闭资源，减少开销
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

}
