package com.cell.usermanage.controller;

import com.cell.usermanage.bean.User;
import com.cell.usermanage.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String list(Model model) {
        // 查询数据库，获取用户列表 List 集合
        List<User> users = userDao.selectAll();
        // 将用户列表存储到 request 域中
        model.addAttribute("users", users);
        // 转发到视图
        return "user_list";
    }

    @PostMapping("/user")
    public String save(User user){
        // 保存用户
        userDao.save(user);
        // 重定向到列表
        return "redirect:/user";
    }

    @GetMapping("/user/{id}")
    public String toUpdate(@PathVariable("id") Long id, Model model){
        // 根据id查询用户信息
        User user = userDao.selectById(id);
        // 将对象存储到request域
        model.addAttribute("user", user);
        // 跳转视图
        return "user_edit";
    }

    @PutMapping("/user")
    public String modify(User user){ // SpringMVC 自动将表单中的字段封装进user对象
        // 更新数据
        userDao.update(user);
        // 重定向
        return "redirect:/user";
    }

    @DeleteMapping("/user/{id}")
    public String del(@PathVariable("id") Long id){
        // 删除
        userDao.deleteById(id);
        // 重定向
        return "redirect:/user";
    }
}
