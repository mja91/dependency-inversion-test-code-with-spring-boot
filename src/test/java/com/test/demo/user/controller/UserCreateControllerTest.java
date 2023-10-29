package com.test.demo.user.controller;

import com.test.demo.mock.TestClockHolder;
import com.test.demo.mock.TestContainer;
import com.test.demo.mock.TestUuidHolder;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.domain.request.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserCreateControllerTest {

    @Test
    void 사용자는_회원가입을_할_수_있고_회원가입_후_PENDING_상태이다() {
        // given
        TestContainer testContainer = createTestContainer();
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("tester@test.com")
                .nickname("tester")
                .address("Seoul")
                .build();

        // when
        ResponseEntity<UserResponseDto> result = testContainer.userCreateController.createUser(userCreateDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody().getEmail()).isEqualTo("tester@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("tester");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNull();
        assertThat(testContainer.userRepository.getById(1L).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    private TestContainer createTestContainer() {
        return new TestContainer(
                new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                new TestClockHolder(0L)
        );
    }
}
