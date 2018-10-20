package com.example.demo.tools.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.util.HttpServletUtil;
import com.example.demo.config.aspect.annotation.AccessLimit;
import com.example.demo.tools.service.ImgCheckCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "基础 -- 图片验证码")
@RestController
@RequestMapping("imgCode")
public class ImgCodeController {
	@Resource(name = "stringRedisTemplate")
	private RedisTemplate<String,String> redisTemplate;
	@Resource
	private ImgCheckCodeService imgCheckCodeService;

	@AccessLimit(maxCount = 20,seconds = 20)
	@ApiOperation(value = "图片验证码",notes = "返回图片验证码并记录到redis")
	@RequestMapping(value = "/checkCode", method = {RequestMethod.GET})
	public void checkCode(HttpServletRequest req, HttpServletResponse resp){
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "No-cache");
		resp.setDateHeader("Expires", 0);
		String checkcode = null;
		try {
			checkcode = imgCheckCodeService.codeCreate(resp.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(checkcode != null){
			String ip = HttpServletUtil.getCustomerIP(req);
			redisTemplate.opsForValue().set("imgCode:" + ip,checkcode,5L,TimeUnit.MINUTES);
		}
	}

}
