package com.example.demo.user.task;

import com.example.demo.tools.service.EmailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 异步发送邮件
 */
@Component
public class EmailSendTask {
    @Resource
    private EmailService emailService;

    /**
     * 给注册成功的用户发送简单的邮件
     *
     * @param receiver 接收邮件的邮箱
     */
    @Async//异步执行的注解，需要启动内加上@EnableAsync
    public void sendSimpleEmail(String receiver){
        long begin = System.currentTimeMillis();
        String subject = "主题:藜麦图书借阅系统注册成功通知";
        String text = "注册成功后请务必修改密码，每次借阅限期为一周，请及时归还；" +
                "若到期还需要继续使用，请登录图书借阅系统续借，祝您生活愉快，工作顺利";
        emailService.sendSimpleMail(receiver,subject,text);
        System.out.println("sendSimpleEmail use: " + (System.currentTimeMillis() - begin) + " milliSeconds");
    }

    /**
     * 给注册成功的用户发送html邮件
     *
     * @param receiver 接收邮件的邮箱
     */
    @Async//异步执行的注解，需要启动内加上@EnableAsync
    public void sendHtmlEmail(String receiver){
        long begin = System.currentTimeMillis();
        String subject = "主题:藜麦图书借阅系统注册成功通知";
        String htmlText = "<font color='red'>注册成功后请务必修改密码，每次借阅限期为一周，请及时归还；" +
                "若到期还需要继续使用，请登录图书借阅系统续借，祝您生活愉快，工作顺利</font>";
        emailService.sendHtmlMail(receiver,subject,htmlText);
        System.out.println("sendHtmlEmail use: " + (System.currentTimeMillis() - begin) + " milliSeconds");
    }
}
