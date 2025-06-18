package com.cell.springmvc.controller;

import org.myspringmvc.stereotype.Controller;
import org.myspringmvc.ui.ModelMap;
import org.myspringmvc.web.bind.annotation.RequestMapping;
import org.myspringmvc.web.bind.annotation.RequestMethod;

@Controller
public class OrderController {
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(ModelMap modelMap) {
        modelMap.addAttribute("orderNo", "123456789");
        return "detail";
    }
}
