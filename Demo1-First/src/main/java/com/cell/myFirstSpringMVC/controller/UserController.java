package com.cell.myFirstSpringMVC.controller;

import com.cell.myFirstSpringMVC.pojo.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;

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

    @RequestMapping(value = "/testParams", params = {"username!=admin", "password"})
    public String testParams() {
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

    @PostMapping(value = "/registerServlet")
    public String registerServlet(HttpServletRequest request) {
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
            @RequestParam(value = "username")
            String username,
            @RequestParam(value = "password")
            String password,
            @RequestParam(value = "sex")
            String sex,
            @RequestParam(value = "hobby")
            String[] hobby,
            @RequestParam(name = "intro")
            String intro) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(sex);
        System.out.println(Arrays.toString(hobby));
        System.out.println(intro);
        return "success";
    }

    @PostMapping("/registerPojo")
    public String register(User user) {
        System.out.println(user);
        return "success";
    }

    @GetMapping("/registerCookie")
    public String register(User user,
                           @RequestHeader(value = "Referer", required = false, defaultValue = "")
                           String referer,
                           @CookieValue(value = "id", required = false, defaultValue = "2222222222")
                           String id) {
        System.out.println(user);
        System.out.println(referer);
        System.out.println(id);
        return "success";
    }

    @RequestMapping("/testServletAPI")
    public String testServletAPI(HttpServletRequest request) {
        // 向request域中存储数据
        request.setAttribute("testRequestScope", "在SpringMVC中使用原生Servlet API实现request域数据共享");
        return "view";
    }

    @RequestMapping("/testModel")
    public String testModel(Model model) {
        // 向request域中存储数据
        model.addAttribute("testRequestScope", "在SpringMVC中使用Model接口实现request域数据共享");
        System.out.println(model);
        System.out.println(model.getClass().getName());
        return "view";
    }

    @RequestMapping("/testMap")
    public String testMap(Map<String, Object> map){
        // 向request域中存储数据
        map.put("testRequestScope", "在SpringMVC中使用Map接口实现request域数据共享");
        System.out.println(map);
        System.out.println(map.getClass().getName());
        return "view";
    }

    @RequestMapping("/testModelMap")
    public String testModelMap(ModelMap modelMap){
        // 向request域中存储数据
        modelMap.addAttribute("testRequestScope", "在SpringMVC中使用ModelMap实现request域数据共享");
        System.out.println(modelMap);
        System.out.println(modelMap.getClass().getName());
        return "view";
    }

    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView(){
        // 创建“模型与视图对象”
        ModelAndView modelAndView = new ModelAndView();
        // 绑定数据
        modelAndView.addObject("testRequestScope", "在SpringMVC中使用ModelAndView实现request域数据共享");
        // 绑定视图
        modelAndView.setViewName("view");
        // 返回
        return modelAndView;
    }

    @RequestMapping("/testSessionScope1")
    public String testServletAPI(HttpSession session) {
        // 向会话域中存储数据
        session.setAttribute("testSessionScope1", "使用原生Servlet API实现session域共享数据");
        return "view";
    }

    @RequestMapping("/testApplicationScope")
    public String testApplicationScope(HttpServletRequest request){
        // 获取ServletContext对象
        ServletContext application = request.getServletContext();
        // 向应用域中存储数据
        application.setAttribute("applicationScope", "应用域中的一条数据");
        return "view";
    }

}
