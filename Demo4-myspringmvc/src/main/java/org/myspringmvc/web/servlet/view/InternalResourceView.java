package org.myspringmvc.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myspringmvc.web.servlet.View;

import java.util.Map;

// 主要负责将控制器返回的逻辑视图名（如 "login"）转发到实际的 JSP 页面（如 /WEB-INF/jsp/login.jsp）上进行页面展示
public class InternalResourceView implements View {
    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    }
}
