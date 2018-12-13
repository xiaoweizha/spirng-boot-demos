package com.example.demo.config.mvc.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.dto.BaseResponse;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;

/**
 * 限流拦截器
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
//    private static final RateLimiter rateLimiter = RateLimiter.create(0.5);//1秒钟最多允许0.5次服务调用 -- 测试用
    private final RateLimiter rateLimiter = RateLimiter.create(1000);//1秒钟最多允许1000次服务调用

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(!(o instanceof HandlerMethod)) return true;
        System.out.println(rateLimiter.acquire());
        System.out.println(rateLimiter.tryAcquire());
        if(rateLimiter.tryAcquire()){
            return true;
        }
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(-4);
        baseResponse.setMsg("当前服务器访问量太大，请稍后再试");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpServletResponse.getOutputStream());
        outputStreamWriter.write(JSONObject.toJSONString(baseResponse));
        outputStreamWriter.flush();
        outputStreamWriter.close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
