package com.cell.spring_interceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class Interceptor2 implements HandlerInterceptor {
    // 1. 前置处理（Controller 方法调用前）
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("LoginInterceptor -> preHandle()");

        return true;
    }

    // 2. 后置处理（Controller 方法调用后，视图渲染前）
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("LoginInterceptor -> postHandle()");
    }

    // 3. 完成处理（视图渲染后）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("LoginInterceptor -> afterCompletion()");
    }
}
