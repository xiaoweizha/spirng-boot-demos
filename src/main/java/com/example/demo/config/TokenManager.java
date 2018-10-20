package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class TokenManager {
    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    private static RedisTemplate<String, String> staticRedisTemplate;

    @PostConstruct//静态方法引用bean用此注解和方法
    public void init() {
        staticRedisTemplate = redisTemplate;
    }

    /**
     * 验证token
     * @param userId 用户id
     * @param token token
     * @return true:成功，false:失败
     */
    public static boolean validateToken(int userId, String token) {
        String tokenInCache = staticRedisTemplate.opsForValue().get("token:" + userId);
        return token.equals(tokenInCache);
    }

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    public String generateToken(int userId) {
        String src = String.valueOf(userId) + System.currentTimeMillis();
        String token = DigestUtils.md5DigestAsHex(src.getBytes());
        redisTemplate.opsForValue().set("token:" + userId, token,60, TimeUnit.MINUTES);
        logger.info("生成token，userId:{}，token:{}", userId, token);
        return token;
    }
}
