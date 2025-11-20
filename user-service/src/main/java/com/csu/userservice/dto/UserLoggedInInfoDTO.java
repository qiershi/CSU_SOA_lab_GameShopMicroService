package com.csu.userservice.dto;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @ProjectName: user-service
* @Title: UserDTO
* @Package: com.csu.userservice.dto
* @Description: user's DTO
* @author qiershi
* @date 2025/11/16 21:06
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/

@Data
public class UserLoggedInInfoDTO {

    private int id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    public UserLoggedInInfoDTO(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    @Constraint(validatedBy = PasswordConstraintValidator.class)
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PasswordConstraint {
        String message() default "密码必须包含字母和数字";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

    }
    public static class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {
        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            if (password == null) {
                return false;
            }
            return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotBlank(message = "用户名不能为空") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "用户名不能为空") String username) {
        this.username = username;
    }

    public @NotBlank(message = "密码不能为空") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "密码不能为空") String password) {
        this.password = password;
    }

    public @Email(message = "邮箱格式不正确") @NotBlank(message = "邮箱不能为空") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "邮箱格式不正确") @NotBlank(message = "邮箱不能为空") String email) {
        this.email = email;
    }
}