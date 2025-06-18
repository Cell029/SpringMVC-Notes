package org.myspringmvc.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.myspringmvc.web.servlet.View;

import java.util.Map;

// 主要负责将控制器返回的逻辑视图名（如 "login"）转发到实际的 JSP 页面（如 /WEB-INF/jsp/login.jsp）上进行页面展示
public class InternalResourceView implements View {

    // 响应的内容类型
    private String contentType;
    // 响应的路径
    private String path;

    public InternalResourceView() {
    }

    public InternalResourceView(String contentType, String path) {
        this.contentType = contentType;
        this.path = path;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /*@Override
    public String getContentType() {
        // jsp 的内容类型
        return "text/html;charset=utf-8";
    }*/

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的内容类型
        response.setContentType(contentType);
        // 将 model 中的所有键值对存入 request 域中（默认存在 request 域中）
        model.forEach(request::setAttribute);
        // 转发（默认是以转发的形式跳转视图）
        request.getRequestDispatcher(path).forward(request, response);
    }
}
