package com.cell.usermanage.controller;

import com.cell.usermanage.bean.User;
import com.cell.usermanage.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}
