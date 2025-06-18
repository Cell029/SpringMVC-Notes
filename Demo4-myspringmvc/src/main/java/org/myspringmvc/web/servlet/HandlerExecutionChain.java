package org.myspringmvc.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class HandlerExecutionChain {
    // 处理器方法，底层为 HandlerMethod 对象
    private Object handler;
    // 拦截器集合
    private List<HandlerInterceptor> interceptorList;
    // 当前拦截器下标
    private int interceptorIndex = -1;

    public HandlerExecutionChain() {
    }

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptorList) {
        this.handler = handler;
        this.interceptorList = interceptorList;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public List<HandlerInterceptor> getInterceptorList() {
        return interceptorList;
    }

    public void setInterceptorList(List<HandlerInterceptor> interceptorList) {
        this.interceptorList = interceptorList;
    }

    public int getInterceptorIndex() {
        return interceptorIndex;
    }

    public void setInterceptorIndex(int interceptorIndex) {
        this.interceptorIndex = interceptorIndex;
    }

    // 执行所有拦截器的 preHandle 方法
    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 顺序遍历拦截器
        for (int i = 0; i < interceptorList.size(); i++) {
            HandlerInterceptor handlerInterceptor = interceptorList.get(i);
            boolean result = handlerInterceptor.preHandle(request, response, handler);
            if (!result) {
                // 执行拦截器的 afterCompletion 方法
                //
                triggerAfterCompletion(request, response, null);
                return false;
            }
            interceptorIndex = i;
        }

        return true;
    }

    // 按照逆序执行拦截其中的所有 PostHandle 方法
    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        for (int i = interceptorList.size() - 1; i >= 0; i--) {
            HandlerInterceptor handlerInterceptor = interceptorList.get(i);
            handlerInterceptor.postHandle(request, response, handler, mv);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        for (int i = interceptorIndex; i >= 0; i--) {
            HandlerInterceptor handlerInterceptor = interceptorList.get(i);
            handlerInterceptor.afterCompletion(request, response, handler, null);
        }
    }
}
