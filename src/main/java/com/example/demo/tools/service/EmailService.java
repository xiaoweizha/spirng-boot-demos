package com.example.demo.tools.service;

public interface EmailService {
    void sendSimpleMail(String receiver,String subject,String text);
    void sendHtmlMail(String receiver,String subject,String text);
}
