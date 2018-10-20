package com.example.demo.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class HttpServletUtil {

    /**
     * 获取客户端ip地址
     */
	public static String getCustomerIP(HttpServletRequest req){
        String ips = req.getHeader("X-Forwarded-For");
        if(StringUtils.hasText(ips) && !"unKnown".equalsIgnoreCase(ips)){
            //多次反向代理后会有多个ip值，第一个才是客户端ip
            int index = ips.indexOf(",");
            if(index != -1){
                return ips.substring(0, index);
            }else{
                return ips;
            }
        }else{
            String ip = req.getHeader("X-Real-IP");
            if(StringUtils.hasText(ip) && !"unKnown".equalsIgnoreCase(ip)){
                return "0:0:0:0:0:0:0:1".equals(ip)?"127.0.0.1":ip;
            }else{
                ip = req.getRemoteAddr();
                return "0:0:0:0:0:0:0:1".equals(ip)?"127.0.0.1":ip;
            }
        }
    }
}
