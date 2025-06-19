package com.cell.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String hello() {
        return "hello";
    }

    @RequestMapping("/index")
    public String text() {
        return "index";
    }
}
