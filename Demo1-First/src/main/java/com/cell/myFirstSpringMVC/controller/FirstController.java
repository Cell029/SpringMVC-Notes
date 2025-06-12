package com.cell.myFirstSpringMVC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FirstController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="/test")
    public String method() {
        System.out.println("正在处理请求。。。");
        // 返回逻辑视图名称（决定跳转到哪个页面）
        return "first";
    }

    @RequestMapping("/other")
    public String other(){
        System.out.println("正在处理其它请求...");
        return "other";
    }
}
