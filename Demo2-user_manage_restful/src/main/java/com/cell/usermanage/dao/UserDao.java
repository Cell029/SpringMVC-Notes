package com.cell.usermanage.dao;

import com.cell.usermanage.bean.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    private static List<User> users = new ArrayList<>();

    static {
        User user1 = new User(10001L, "张三", "zhangsan@qq.com", 1);
        User user2 = new User(10002L, "李四", "lisi@qq.com", 1);
        User user3 = new User(10003L, "王五", "wangwu@qq.com", 1);
        User user4 = new User(10004L, "赵六", "zhaoliu@qq.com", 0);
        User user5 = new User(10005L, "钱七", "qianqi@qq.com", 0);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
    }

    public List<User> selectAll(){
        return users;
    }

    public static Long generateId(){
        // Stream API
        Long maxId = users.stream().map(user -> user.getId()).reduce((id1, id2) -> id1 > id2 ? id1 : id2).get();
        return maxId + 1;
    }

    public void save(User user){
        // 设置id
        user.setId(generateId());
        // 保存
        users.add(user);
    }

    public User selectById(Long id){
        return users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
    }

    public void update(User user){
        for (int i = 0; i < users.size(); i++) {
            if(user.getId().equals(users.get(i).getId())){
                users.set(i, user);
                break;
            }
        }
    }

    public void deleteById(Long id){
        for (int i = 0; i < users.size(); i++) {
            if(id.equals(users.get(i).getId())){
                users.remove(i);
                break;
            }
        }
    }

}
