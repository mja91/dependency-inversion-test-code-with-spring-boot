package com.test.demo.user.controller;

import com.test.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.test.demo.common.domain.exception.ResourceNotFoundException;
import com.test.demo.mock.TestClockHolder;
import com.test.demo.mock.TestContainer;
import com.test.demo.mock.TestUuidHolder;
import com.test.demo.user.controller.response.MyProfileResponseDto;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserUpdateDto;
import com.test.demo.user.domain.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_조회할_때_개인정보는_제외된채_응답을_반환_받을_수_있다() {
        // given
        TestContainer testContainer = createTestContainer();
        testContainer.userRepository.save(
                User.builder()
                    .id(1L)
                    .email("tester@test.com")
                    .nickname("tester")
                    .address("Seoul")
                    .status(UserStatus.ACTIVE)
                    .lastLoginAt(100L)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .build()
        );

        // when
        ResponseEntity<UserResponseDto> result = testContainer.userController.getUserById(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("tester@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("tester");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_API를_호출할_경우_404_응답을_받는다() {
        // given
        TestContainer testContainer = createTestContainer();

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(1L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        // given
        TestContainer testContainer = createTestContainer();
        testContainer.userRepository.save(
                User.builder()
                    .id(1L)
                    .email("tester@test.com")
                    .nickname("tester")
                    .status(UserStatus.PENDING)
                    .lastLoginAt(null)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .build()
        );

        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_반환_받는다() {
        // given
        TestContainer testContainer = createTestContainer();
        testContainer.userRepository.save(
                User.builder()
                        .id(1L)
                        .email("tester@test.com")
                        .nickname("tester")
                        .status(UserStatus.PENDING)
                        .lastLoginAt(null)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build()
        );

        // when
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(1L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보로_주소도_갖고_올_수_있다() {
        // given
        TestContainer testContainer = createTestContainer();
        testContainer.userRepository.save(
                User.builder()
                    .id(1L)
                    .email("tester@test.com")
                    .nickname("tester")
                    .address("Seoul")
                    .status(UserStatus.ACTIVE)
                    .lastLoginAt(100L)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .build()
        );

        // when
        ResponseEntity<MyProfileResponseDto> result = testContainer.userController.getMyInfo("tester@test.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getEmail()).isEqualTo("tester@test.com");
        assertThat(result.getBody().getNickname()).isEqualTo("tester");
        assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() {
        // given
        TestContainer testContainer = createTestContainer();
        testContainer.userRepository.save(
                User.builder()
                    .id(1L)
                    .email("tester@test.com")
                    .nickname("tester")
                    .address("Seoul")
                    .status(UserStatus.ACTIVE)
                    .lastLoginAt(100L)
                    .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .build()
        );
        final String myEmail = "tester@test.com";
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("tester2")
                .address("Jeju")
                .build();

        // when
        ResponseEntity<MyProfileResponseDto> result = testContainer.userController.updateMyInfo(myEmail, userUpdateDto);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getNickname()).isEqualTo("tester2");
        assertThat(result.getBody().getAddress()).isEqualTo("Jeju");
    }
    
    private TestContainer createTestContainer() {
        return new TestContainer(
                new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                new TestClockHolder(100L)
        );
    }
}
