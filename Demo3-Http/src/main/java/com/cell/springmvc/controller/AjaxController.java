package com.cell.springmvc.controller;

import com.cell.springmvc.bean.User;
import com.cell.springmvc.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class AjaxController {
    /*@GetMapping(value = "/ajax")
    public void hello(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("hello");
    }*/

    @RequestMapping(value = "/ajax")
    public User hello() {
        User user = new User("zhangsan", "22222");
        return user;
    }

    @RequestMapping("/send")
    public void send(@RequestBody User user) {
        System.out.println(user);
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
    }

    @RequestMapping("/user")
    public String send(RequestEntity<User> requestEntity) {
        System.out.println("请求方式：" + requestEntity.getMethod());
        System.out.println("请求URL：" + requestEntity.getUrl());
        HttpHeaders headers = requestEntity.getHeaders();
        System.out.println("请求的内容类型：" + headers.getContentType());
        System.out.println("请求头：" + headers);

        User user = requestEntity.getBody();
        System.out.println(user);
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        return "success";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(user);
        }
    }


}
