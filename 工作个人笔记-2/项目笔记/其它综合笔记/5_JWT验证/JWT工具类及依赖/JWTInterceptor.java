package com.jeesite.modules.weixin.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.common.util.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义JWT拦截器
 * */
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,Object>  map = new HashMap<>();
        String token = request.getHeader("token");
        try{
            JWTUtils.verify(token);
            return  true;
        } catch (SignatureVerificationException e){
            e.printStackTrace();
            map.put("message","invalid signature");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("message","token has already expired");
        } catch (AlgorithmMismatchException e){
            e.printStackTrace();
            map.put("message","algorithm mismatch");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message","invalid token");
        }
        map.put("success",false);
        /*response.setContentType("application/json;charset=UTF-8");
        OutputStream out = null;

        try {
            out = response.getOutputStream();
            out.write(JSONObject.toJSONString(map).getBytes());
            out.flush();
        } finally {
            if(out != null){
                out.close();
            }
        }*/
        //使用工具类返回响应数据
        ServletUtils.renderString(response,JSONObject.toJSONString(map),"application/json;charset=UTF-8");
        return false;
    }
}
