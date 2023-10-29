package com.test.demo.user.domain;

import com.test.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.test.demo.common.service.port.ClockHolder;
import com.test.demo.common.service.port.UuidHolder;
import com.test.demo.user.domain.request.UserCreateDto;
import com.test.demo.user.domain.request.UserUpdateDto;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.infrastructure.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String address;
    private final String certificationCode;
    private final UserStatus status;
    private final Long lastLoginAt;

    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreateDto userCreateDto, UuidHolder uuidHolder) {
        return User.builder()
            .email(userCreateDto.getEmail())
            .nickname(userCreateDto.getNickname())
            .address(userCreateDto.getAddress())
            .status(UserStatus.PENDING)
            .certificationCode(uuidHolder.random())
            .build();
    }

    public User update(UserUpdateDto userUpdateDto) {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(userUpdateDto.getNickname())
            .address(userUpdateDto.getAddress())
            .certificationCode(certificationCode)
            .status(status)
            .lastLoginAt(lastLoginAt)
            .build();
    }

    public User login(ClockHolder clockHolder) {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .address(address)
            .certificationCode(certificationCode)
            .status(UserStatus.ACTIVE)
            .lastLoginAt(clockHolder.millis())
            .build();
    }

    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .address(address)
            .certificationCode(certificationCode)
            .status(UserStatus.ACTIVE)
            .lastLoginAt(lastLoginAt)
            .build();
    }
}
