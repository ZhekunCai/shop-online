package com.shoponline.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * 用户登录和退出记录
 */
@Data
public class LoginRecord {
/*
    private Long id;

    private LogType type;

    private String ip;

    private Date operatedAt;

    private enum LogType {
        LOG_IN, LOG_OUT,
    }
    */
    @NotNull(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
