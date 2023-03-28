package com.jeesite.modules.weixin.interceptor;

import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.flyl.entity.FlylUser;
import com.jeesite.modules.weixin.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        FlylUser loginUser = tokenService.getLoginUser(request);
        if(loginUser != null){
            tokenService.verifyToken(loginUser);
            return true;
        }
        ServletUtils.renderString(response,"{\"data\":\"token验证失败，或已过期，请重新登录\"}");
        return false;
    }
}
