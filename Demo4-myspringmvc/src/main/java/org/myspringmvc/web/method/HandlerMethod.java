package org.myspringmvc.web.method;

import java.lang.reflect.Method;

public class HandlerMethod {
    // 处理器对象
    private Object handler;
    // 要执行的方法
    private Method method;

    public HandlerMethod() {
    }

    public HandlerMethod(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
