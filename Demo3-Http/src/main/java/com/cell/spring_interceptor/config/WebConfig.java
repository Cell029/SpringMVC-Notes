package com.cell.spring_interceptor.config;

import com.cell.spring_interceptor.interceptor.Interceptor2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private Interceptor2 interceptor2;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor2)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/login", "/css/**", "/js/**", "/images/**"); // 放行登录页、静态资源
    }
}
