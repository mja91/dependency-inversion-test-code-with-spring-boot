package com.test.demo.user.service.port;

public interface MailSender {

    void send(String email, String title, String content);
}
