package com.csu.userservice.controller;

import com.csu.userservice.dto.LoginUserDTO;
import com.csu.userservice.dto.RegisterUserDTO;
import com.csu.userservice.entity.User;
import com.csu.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
* @ProjectName: user-service
* @Title: UserController
* @Package: com.csu.userservice.controller
* @Description: user's controller
* @author qiershi
* @date 2025/11/16 20:38
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public Map<String, Object> register(@RequestParam RegisterUserDTO registerUserDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(registerUserDTO.getUsername());
            user.setPassword(registerUserDTO.getPassword());
            user.setEmail(registerUserDTO.getEmail());

            userService.register(user);
            response.put("message", "注册成功");
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/user")
    public Map<String, Object> login(@RequestParam LoginUserDTO loginUserDTO) {
        System.out.println(loginUserDTO.toString());
        Map<String, Object> response = new HashMap<>();
        try {
            String username = loginUserDTO.getUsername();
            String password = loginUserDTO.getPassword();

            if (username == null || password == null) {
                response.put("code", 400);
                response.put("message", "名称或密码不能为空");
                return response;
            }

            Map<String, Object> result = userService.login(username, password);

            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", result.get("user"));
            response.put("token", result.get("token"));
        } catch (IllegalArgumentException e) {
            response.put("code", 401);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "服务器错误：" + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/user/{id}")
    public Map<String, Object> updateUserInfo(@PathVariable int id, @Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            int result = userService.updateUserInfo(id,user);
            if (result > 0) {
                response.put("message", "更新用户信息成功");
                response.put("success", true);
            } else {
                response.put("message", "更新失败，可能是数据未发生变化");
                response.put("success", false);
            }
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/user/{id}")
    public Map<String, Object> getUserInfo(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
//            if (id == null || id.trim().isEmpty()) {
//                response.put("message", "缺少 id 参数");
//                response.put("success", false);
//                return response;
//            }

            int userId = id;
//            try {
//                userId = Integer.parseInt(id);
//            } catch (NumberFormatException e) {
//                response.put("message", "id 参数必须是有效的整数");
//                response.put("success", false);
//                return response;
//            }

            User user = userService.getUserInfo(userId);
            response.put("data", user);
            response.put("success", true);
        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
        } catch (Exception e) {
            response.put("message", "服务器错误：" + e.getMessage());
            response.put("success", false);
            e.printStackTrace();
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

}