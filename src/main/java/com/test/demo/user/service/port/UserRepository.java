package com.test.demo.user.service.port;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.enums.UserStatus;

import java.util.Optional;

public interface UserRepository {

    User getById(long id);

    Optional<User> findById(long id);

    Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(User user);

}
