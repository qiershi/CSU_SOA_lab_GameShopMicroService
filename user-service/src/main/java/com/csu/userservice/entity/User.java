package com.csu.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
* @ProjectName: user-service
* @Title: User
* @Package: com.csu.userservice.entity
* @Description: user
* @author qiershi
* @date 2025/11/16 20:41
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/

@Entity
@Data
@TableName("user")
public class User {
    @TableId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String username;
    private String password;
    private String email;

    public User(){}

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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