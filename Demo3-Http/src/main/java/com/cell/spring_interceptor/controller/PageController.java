package com.cell.spring_interceptor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 登录表单提交处理
    @PostMapping("/doLogin")
    public String doLogin(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        // 简单模拟验证，真实情况用数据库验证
        if ("admin".equals(username) && "123".equals(password)) {
            // 登录成功，保存用户到 session
            request.getSession().setAttribute("loginUser", username);
            return "redirect:/home";  // 登录成功跳主页
        }
        // 登录失败，回登录页（可加错误提示）
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

}
