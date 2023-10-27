package com.test.demo.user.service.port;

import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.repository.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findById(long id);

    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

}
