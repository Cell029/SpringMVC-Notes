package org.myspringmvc.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    // 根据请求获取处理器执行链
    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
