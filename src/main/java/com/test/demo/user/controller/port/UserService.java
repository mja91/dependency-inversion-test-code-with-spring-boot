package com.test.demo.user.controller.port;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserCreateDto;
import com.test.demo.user.domain.request.UserUpdateDto;

public interface UserService {

    User getByEmail(String email);

    User getById(long id);

    User create(UserCreateDto userCreateDto);

    User update(long id, UserUpdateDto userUpdateDto);

    void login(long id);

    void verifyEmail(long id, String certificationCode);
}