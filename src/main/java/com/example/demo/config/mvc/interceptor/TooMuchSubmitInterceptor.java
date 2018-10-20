package com.example.demo.config.mvc.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.dto.BaseResponse;
import com.example.demo.common.util.HttpServletUtil;
import com.example.demo.config.aspect.annotation.AccessLimit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

/**
 * 防止恶意刷接口的拦截器
 */
@Component
public class TooMuchSubmitInterceptor implements HandlerInterceptor {
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(!(o instanceof HandlerMethod)) return true;
        HandlerMethod method = (HandlerMethod)o;
        if(method.hasMethodAnnotation(AccessLimit.class)){
            String ip = HttpServletUtil.getCustomerIP(httpServletRequest);
            String key = ip + ":" + httpServletRequest.getServletPath() + ":" + httpServletRequest.getMethod();
            String string = redisTemplate.opsForValue().get(key);
            Integer count = Integer.valueOf(string == null? "0":string);
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            int time = accessLimit.seconds();
            int countRequired = accessLimit.maxCount();
            if(count > countRequired){
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setCode(-4);
                baseResponse.setMsg("请求过于频繁，请" + time + "秒后再试");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpServletResponse.getOutputStream());
                outputStreamWriter.write(JSONObject.toJSONString(baseResponse));
                outputStreamWriter.flush();
                outputStreamWriter.close();
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if(!(o instanceof HandlerMethod)) return;
        HandlerMethod method = (HandlerMethod)o;
        if(method.hasMethodAnnotation(AccessLimit.class)){
            String ip = HttpServletUtil.getCustomerIP(httpServletRequest);
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            int time = accessLimit.seconds();
            String key = ip + ":" + httpServletRequest.getServletPath() + ":" + httpServletRequest.getMethod();
            redisTemplate.opsForValue().increment(key,1);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }
}
