package com.test.demo.user.controller.port;

public interface CertificationService {

    void send(String email, long userId, String certificationCode);
}
