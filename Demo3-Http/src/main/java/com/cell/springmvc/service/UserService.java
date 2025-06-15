package com.cell.springmvc.service;

import com.cell.springmvc.bean.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User getById(long id) {
        if (id == 1) {
            return new User("zhangsan", "12312");
        } else {
            return null;
        }
    }
}
