package com.cell.myFirstSpringMVC.controller;

import com.cell.myFirstSpringMVC.pojo.User;import jakarta.servlet.http.HttpServletRequest;import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

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

    @RequestMapping(value = "/headerTest", headers = "Content-Type!=application/json")
    public String jsonOnly() {
        return "headerTest";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping(value="/registerServlet")
    public String registerServlet(HttpServletRequest request){
        // 通过当前请求对象获取提交的数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String[] hobbies = request.getParameterValues("hobby");
        String intro = request.getParameter("intro");
        System.out.println(username + "," + password + "," + sex + "," + Arrays.toString(hobbies) + "," + intro);
        return "success";
    }

    @PostMapping(value = "/registerParam")
    public String register(
            @RequestParam(value="username")
            String username,
            @RequestParam(value="password")
            String password,
            @RequestParam(value="sex")
            String sex,
            @RequestParam(value="hobby")
            String[] hobby,
            @RequestParam(name="intro")
            String intro) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(sex);
        System.out.println(Arrays.toString(hobby));
        System.out.println(intro);
        return "success";
    }

    @PostMapping("/registerPojo")
    public String register(User user){
        System.out.println(user);
        return "success";
    }
}
