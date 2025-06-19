package com.cell.ssm.dao;

import com.cell.ssm.bean.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    @Select("select * from t_user where id = #{id}")
    User selectById(Long id);
}
