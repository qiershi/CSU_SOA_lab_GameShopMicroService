package com.csu.userservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.userservice.dto.UserDTO;
import com.csu.userservice.entity.User;
import com.csu.userservice.mapper.UserMapper;
import com.csu.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @ProjectName: user-service
* @Title: UserService
* @Package: com.csu.userservice.service
* @Description: user's service
* @author qiershi
* @date 2025/11/16 21:15
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public boolean matches(String password, String storedPwd) {
        return password.equals(storedPwd);
    }

    public void register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        // 设置默认密码
        if (user.getPassword() == null) {
            user.setPassword("123456"); // 明文默认密码
            // 或使用加密密码
            // user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        }
        userMapper.insert(user);
    }

    public Map<String, Object> login(String username, String password) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username);
//        User user = userMapper.selectOne(queryWrapper);
        User user = userMapper.GetUserByName(username);

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!matches(password, user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        String token = jwtUtil.generateToken(Math.toIntExact(user.getId()));
        Map<String, Object> result = new HashMap<>();
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        result.put("user", userDTO); // 使用 DTO 避免返回密码
        result.put("token", token);
        return result;
    }

    public int updateUserInfo(int id,User user) {
        User existingUser = userMapper.GetUserById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("商家不存在");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (existingUser != null && existingUser.getId() != id) {
            throw new IllegalArgumentException("用户名已存在");
        }

        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(user.getPassword());
        }

        userMapper.update(user);
        return userMapper.update(user);
    }

    public User getUserInfo(int id) {
        User user= userMapper.GetUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return user;
    }

    public void deleteUser(int id) {
        User user = userMapper.GetUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        userMapper.deleteById(id);
    }
}