package com.csu.userservice.dto;

import lombok.Data;

/**
* @ProjectName: user-service
* @Title: RegisterUserDTO
* @Package: com.csu.userservice.dto
* @Description: the data frontend transferred when user's registering
* @author qiershi
* @date 2025/11/16 21:25
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}