package com.cell.springmvc.controller;

import org.myspringmvc.stereotype.Controller;
import org.myspringmvc.ui.ModelMap;
import org.myspringmvc.web.bind.annotation.RequestMapping;
import org.myspringmvc.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    @RequestMapping(value ="/", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        // 向 request 域存储数据
        modelMap.addAttribute("username", "lisi");
        return "index";
    }
}
