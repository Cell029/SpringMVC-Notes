package com.cell.springmvc.controller;

import org.myspringmvc.stereotype.Controller;
import org.myspringmvc.web.bind.annotation.RequestMapping;
import org.myspringmvc.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    @RequestMapping(value ="/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
