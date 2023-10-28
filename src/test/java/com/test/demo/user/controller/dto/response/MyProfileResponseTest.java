package com.test.demo.user.controller.dto.response;

import com.test.demo.user.controller.response.MyProfileResponseDto;
import com.test.demo.user.domain.User;
import com.test.demo.user.domain.enums.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MyProfileResponseTest {

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
        MyProfileResponseDto myProfileResponseDto = MyProfileResponseDto.from(user);

        // then
        assertThat(myProfileResponseDto.getId()).isEqualTo(1L);
        assertThat(myProfileResponseDto.getEmail()).isEqualTo("tester3@test.com");
        assertThat(myProfileResponseDto.getNickname()).isEqualTo("tester3");
        assertThat(myProfileResponseDto.getAddress()).isEqualTo("Seoul");
        assertThat(myProfileResponseDto.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponseDto.getLastLoginAt()).isEqualTo(100L);
    }
}
