package com.test.demo.user.domain;

import com.test.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.test.demo.mock.TestClockHolder;
import com.test.demo.mock.TestUuidHolder;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.domain.request.UserCreateDto;
import com.test.demo.user.domain.request.UserUpdateDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest {

    @Test
    public void User는_UserCreateDto_객체로_생성할_수_있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("tester@test.com")
                .nickname("tester")
                .address("Seoul")
                .build();

        // when
        User user = User.from(userCreateDto, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("tester@test.com");
        assertThat(user.getNickname()).isEqualTo("tester");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void User는_UserUpdateDto_객체로_생성할_수_있다() {
        // given
        User user = createActiveStatusTestUser();
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("tester3")
                .address("Jeju")
                .build();

        // when
        user = user.update(userUpdateDto);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("tester@test.com");
        assertThat(user.getNickname()).isEqualTo("tester3");
        assertThat(user.getAddress()).isEqualTo("Jeju");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    public void User는_로그인을_할_수_있고_로그인_시_마지막_로그인_시간이_변경된다() {
        // given
        User user = createActiveStatusTestUser();

        // when
        user = user.login(new TestClockHolder(1678530673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    public void User는_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = createPendingStatusTestUser();

        // when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User는_잘못된_인증_코드로_계정을_활성화_하려고하면_에러가_발생된다() {
        // given
        User user = createPendingStatusTestUser();

        // when
        // then
        assertThatThrownBy(() -> user.certificate("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    private User createActiveStatusTestUser() {
        return User.builder()
                .id(1L)
                .email("tester@test.com")
                .nickname("tester")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
    }

    private User createPendingStatusTestUser() {
        return User.builder()
                .id(1L)
                .email("tester@test.com")
                .nickname("tester")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
    }
}
