package com.cell.exception_resolver.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice // 这是一个全局异常处理类
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class) // 捕获所有 Exception 异常
    public ModelAndView handleException(Exception e) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error"); // 设置视图名：error.html
        mav.addObject("e", e); // 把异常信息传给视图
        return mav;
    }
}
