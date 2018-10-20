package com.example.demo.user.controller;

import com.example.demo.common.controller.BaseController;
import com.example.demo.common.dto.BaseResponse;
import com.example.demo.config.aspect.annotation.AccessLimit;
import com.example.demo.config.aspect.annotation.AutoValidate;
import com.example.demo.config.aspect.annotation.LogForController;
import com.example.demo.user.dto.request.RegisterRequest;
import com.example.demo.user.dto.response.LoginResponse;
import com.example.demo.user.service.UserService;
import com.example.demo.user.task.EmailSendTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*",allowedHeaders = "Content-Type,Access-Token,x-requested-with",allowCredentials = "true",maxAge = 3600,exposedHeaders = "content-type")
@RestController
@RequestMapping("user")
@Api(description = "基础 -- 用户")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private EmailSendTask emailSendTask;

    //@TokenValidate-----登录状态才可以访问的接口加此注解，并添加userId和token入参
    @LogForController
    @AccessLimit//默认一个客户端在5秒内最多允许调用5次
    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<LoginResponse> login(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password) {
        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
        LoginResponse loginResponse = userService.login(userName,password);
        if(loginResponse != null){
            baseResponse.setCode(0);
            baseResponse.setMsg("登录成功");
            baseResponse.setData(loginResponse);
        }else{
            baseResponse.setCode(1);
            baseResponse.setMsg("用户名或密码不正确");
        }
        return baseResponse;
    }

    @AutoValidate//校验入参实体类的属性表达式是否合法
    @LogForController//入参/出参自动写日志
    @AccessLimit(seconds = 60,maxCount = 6)//同一个客户端60秒内最多允许调用接口6次
    @ApiOperation(value = "注册", notes = "注册")//swagger注释
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResponse<Object> register(@RequestBody RegisterRequest registerRequest) {
        BaseResponse<Object> baseResponse = new BaseResponse<>();
        Integer flag = userService.register(registerRequest);
        if(flag == 0){
            baseResponse.setCode(0);
            String email = registerRequest.getEmail();
            emailSendTask.sendSimpleEmail(email);
            emailSendTask.sendHtmlEmail(email);//发一个邮件就可以了
            baseResponse.setMsg("注册成功");
        }else if(flag == 1){
            baseResponse.setCode(1);
            baseResponse.setMsg("手机号已被注册");
        }else{
            baseResponse.setCode(2);
            baseResponse.setMsg("邮箱已被注册");
        }
        return baseResponse;
    }

}
