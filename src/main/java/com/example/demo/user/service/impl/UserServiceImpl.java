package com.example.demo.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.TokenManager;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.LoginResponse;
import com.example.demo.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TokenManager tokenManager;

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(String userName, String password) {
        String password1 = DigestUtils.md5DigestAsHex(password.getBytes());
        int k = Math.abs((userName + password1).hashCode())%16;
        if(k > 0){
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(k);
            loginResponse.setExpire(System.currentTimeMillis() + 3600 * 1000L);
            String token = tokenManager.generateToken(k);
            loginResponse.setToken(token);
            return loginResponse;
        }
        return null;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor=RuntimeException.class)
    @Override
    public Integer register(RegisterRequest registerRequest) {
        int k = Math.abs(JSONObject.toJSONString(registerRequest).hashCode())%16;
        if(k > 0){
            int a = new Random().nextInt(10);
            if(a > 0){
                RegisterRequest registerRequest1 = new RegisterRequest();
                BeanUtils.copyProperties(registerRequest,registerRequest1);
                String password = DigestUtils.md5DigestAsHex(registerRequest.getPassword().getBytes());
                registerRequest1.setPassword(password);
                System.out.println(registerRequest1);
                return 0;
            }
            return 2;
        }
        return 1;
    }

}
