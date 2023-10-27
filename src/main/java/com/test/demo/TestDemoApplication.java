package com.test.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Test Demo Project",
        version = "v1",
        description = "테스트 코드를 추가하기 위한 연습용 프로젝트 입니다."
))
public class TestDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestDemoApplication.class, args);
    }
}