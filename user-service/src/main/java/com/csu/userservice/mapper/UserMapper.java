package com.csu.userservice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User GetUserById(int id);

    User GetUserByName(String username);

    int insert(User user);

    int update(User user);

    int deleteById(int id);
}
