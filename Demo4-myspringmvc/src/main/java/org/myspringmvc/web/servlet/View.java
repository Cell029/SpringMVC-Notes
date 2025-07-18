package org.myspringmvc.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    // 获取响应的内容类型
    // String getContentType();
    // 渲染
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
