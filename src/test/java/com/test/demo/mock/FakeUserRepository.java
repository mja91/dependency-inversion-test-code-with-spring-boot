package com.test.demo.mock;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong idGenerator = new AtomicLong(0);
    List<User> userList = new ArrayList<>();

    @Override
    public User getById(long id) {
        return userList.stream()
                .filter(u -> u.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public Optional<User> findById(long id) {
        return userList.stream()
                .filter(u -> u.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return userList.stream()
                .filter(u -> u.getId().equals(id) && u.getStatus().equals(userStatus))
                .findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userList.stream()
                .filter(u -> u.getEmail().equals(email) && u.getStatus().equals(userStatus))
                .findAny();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(idGenerator.incrementAndGet())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .status(user.getStatus())
                    .lastLoginAt(user.getLastLoginAt())
                    .certificationCode(user.getCertificationCode())
                    .build();
            userList.add(newUser);
            return newUser;
        } else {
            userList.removeIf(u -> Objects.equals(u.getId(), user.getId()));
            userList.add(user);
            return user;
        }
    }
}
