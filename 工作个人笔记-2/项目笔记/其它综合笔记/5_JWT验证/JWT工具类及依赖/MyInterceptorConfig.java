package com.jeesite.modules.config;

import com.jeesite.modules.weixin.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/wechat/**")       //拦截路径
                .excludePathPatterns("/user/**");    //放行路径，也可不写，不写默认放行
    }

}
