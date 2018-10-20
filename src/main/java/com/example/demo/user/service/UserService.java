package com.example.demo.user.service;

import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.LoginResponse;

public interface UserService {

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 信息
     */
    LoginResponse login(String userName, String password);

    /**
     * 注册
     *
     * @param registerRequest 注册填入的信息
     * @return 标记状态
     */
    Integer register(RegisterRequest registerRequest);
}
