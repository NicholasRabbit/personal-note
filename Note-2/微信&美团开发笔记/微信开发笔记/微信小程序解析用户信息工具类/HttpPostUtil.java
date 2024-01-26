package com.jeesite.modules.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpPostUtil {


    public static HttpEntity doPost(String url, Map<String, Object> map, String charset) {

        //设置请求超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60000)
                .setConnectTimeout(60000)
                .setConnectionRequestTimeout(60000)
                .build();
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        String message = null;
        HttpEntity entity = null;
        try {
            httpClient = new DefaultHttpClient();

            //设置发送的数据
            StringEntity s = new StringEntity(JSON.toJSONString(map),"UTF-8");
            //s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(s);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {  //200表示请求成功
                entity = response.getEntity();
                //message = EntityUtils.toString(entity, "utf-8");

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
        return entity;
    }


    /* 发送 post请求 用HTTPclient 发送请求*/
    public static byte[] post(String URL, Map<String,Object> map) {
        String obj = null;
        InputStream inputStream = null;
        Buffer reader = null;
        byte[] data = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(URL);
        httppost.addHeader("Content-type", "application/json; charset=utf-8");
        httppost.setHeader("Accept", "application/json");
        try {
            StringEntity s = new StringEntity(JSON.toJSONString(map), Charset.forName("UTF-8"));
            s.setContentEncoding("UTF-8");
            httppost.setEntity(s);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 获取相应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    inputStream = entity.getContent();
                    data = readInputStream(inputStream);
                }
                return data;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**  将流 保存为数据数组
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}
