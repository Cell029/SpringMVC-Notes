package org.myspringmvc.web.servlet;

public interface HandlerAdapter {
    // 执行处理器方法
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
