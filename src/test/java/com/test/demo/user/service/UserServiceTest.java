package com.test.demo.user.service;

import com.test.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.test.demo.common.domain.exception.ResourceNotFoundException;
import com.test.demo.user.domain.dto.request.UserCreateDto;
import com.test.demo.user.domain.dto.request.UserUpdateDto;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.yml")
@SqlGroup({
        @Sql("/sql/user-service-test-data.sql"),
        @Sql("/sql/delete-all-data.sql")
})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEamil은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "tester@test.com";

        // when
        UserEntity userEntity = userService.getByEmail(email);

        // then
        assertThat(userEntity.getEmail()).isEqualTo(email);
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        String email = "tester2@test.com";

        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        UserEntity userEntity = userService.getById(1);

        // then
        assertThat(userEntity.getId()).isEqualTo(1);
    }

    @Test
    void getById는_PENDING_상태인_유저를_찾아올_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            UserEntity userEntity = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용하여_유저_정보를_생성할_수_있다() {
        // given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("tester3@test.com")
                .nickname("tester3")
                .address("Seoul")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        UserEntity userEntity = userService.create(userCreateDto);

        // then
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo("tester3@test.com");
        assertThat(userEntity.getNickname()).isEqualTo("tester3");
        assertThat(userEntity.getAddress()).isEqualTo("Seoul");
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void userUpdateDto를_이용하여_유저_정보를_수정할_수_있다() {
        // given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("tester1")
                .address("Jeju")
                .build();

        // when
        userService.update(1, userUpdateDto);

        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getNickname()).isEqualTo("tester1");
        assertThat(userEntity.getAddress()).isEqualTo("Jeju");
    }

    @Test
    void 사용자를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userService.login(1);

        // then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void PENDING_상태의_사용자는_이메일_인증_코드로_ACTIVE_시킬_수_있다() {
        // given
        // when
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        // then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자가_이메일_인증_코드를_잘못_입력한_경우_에러를_반환한다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}
