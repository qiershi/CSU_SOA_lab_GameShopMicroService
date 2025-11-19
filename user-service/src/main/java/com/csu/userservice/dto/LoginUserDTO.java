package com.csu.userservice.dto;

import lombok.Data;

/**
* @ProjectName: user-service
* @Title: LoginUserDTO
* @Package: com.csu.userservice.dto
* @Description: user's login data
* @author qiershi
* @date 2025/11/19 14:11
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/

@Data
public class LoginUserDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginUserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}