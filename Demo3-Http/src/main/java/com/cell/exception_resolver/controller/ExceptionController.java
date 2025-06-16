package com.cell.exception_resolver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @GetMapping("/helloException")
    public String hello() {
        return "ok";
    }

    @RequestMapping("/hello2")
    public String error() {
        // 模拟业务异常
        throw new RuntimeException("GET请求抛出的异常");
    }

}
