package com.test.demo.user.service;

import com.test.demo.common.domain.exception.ResourceNotFoundException;
import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserCreateDto;
import com.test.demo.user.domain.request.UserUpdateDto;
import com.test.demo.user.domain.enums.UserStatus;
import com.test.demo.user.infrastructure.entity.UserEntity;
import com.test.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CertificationService certificationService;

    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }

    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Transactional
    public User create(UserCreateDto userCreateDto) {
        User user = User.from(userCreateDto);
        user = userRepository.save(UserEntity.fromModel(user).toModel());
        certificationService.send(user.getEmail(), user.getId(), user.getCertificationCode());
        return user;
    }

    @Transactional
    public User update(long id, UserUpdateDto userUpdateDto) {
        User user = getById(id);
        user.update(userUpdateDto);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.login();
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        user = user.certificate(certificationCode);
        userRepository.save(user);
    }
}