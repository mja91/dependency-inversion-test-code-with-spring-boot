package com.test.demo.common.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class HealthCheckTest {

    @Test
    void 헬스_체크_시_응답으로_200OK를_반환한다() {
        // given
        HealthCheckController healthCheckController = new HealthCheckController();

        // when
        ResponseEntity<Void> result = healthCheckController.healthCheck();

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }
}