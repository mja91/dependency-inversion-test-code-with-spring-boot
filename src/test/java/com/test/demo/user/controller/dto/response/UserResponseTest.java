package com.test.demo.user.controller.dto.response;

import com.test.demo.mock.TestUuidHolder;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.domain.User;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.domain.request.UserCreateDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserResponseTest {

    @Test
    public void User로_응답을_생성할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("tester3@test.com")
                .nickname("tester3")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        UserResponseDto userResponseDto = UserResponseDto.from(user);

        // then
        assertThat(user.getId()).isEqualTo(0L);
        assertThat(user.getEmail()).isEqualTo("tester3@test.com");
        assertThat(user.getNickname()).isEqualTo("tester3");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo("PENDING");
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}
