package com.cell.myFirstSpringMVC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/testRESTful/{id}/{username}/{age}")
    public String testRESTful(
            @PathVariable("id")
            int id,
            @PathVariable("username")
            String username,
            @PathVariable("age")
            int age) {
        System.out.println(id + "," + username + "," + age);
        return "testRESTful";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login() {
        return "success";
    }

    @RequestMapping(value="/testParams", params = {"username!=admin", "password"})
    public String testParams(){
        return "testParams";
    }
}
