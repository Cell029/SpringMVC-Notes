package org.myspringmvc.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    // DispatcherServlet 被初始化后会调用 init(ServletConfig)
    // 所以会从父类中找，最终执行 HttpServlet 的 init(ServletConfig)
    // 最终在 HttpServletBean 的 init(ServletConfig) 中调用 DispatcherServlet 的 init() 方法
    @Override
    public void init() throws ServletException {
        // 初始化 Spring Web 容器，将所有应该创建的对象全部创建出来交给 IoC 容器管理

    }

    // 只要浏览器发送一次请求，就会调用一次 service 方法
    // 实际上每次请求时先调用 HttpServlet 的 service(ServletRequest req, ServletResponse res)，但这个方法中对 req 和 res 进行了转型
    // 将它们转换成 HttpServletRequest 和 HttpServletResponse
    // 然后传递给子类重写的 service 方法
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    // 处理请求的核心方法
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
