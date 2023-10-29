package com.test.demo.user.controller.port;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserCreateDto;

public interface UserCreateService {

    User create(UserCreateDto userCreateDto);
}
