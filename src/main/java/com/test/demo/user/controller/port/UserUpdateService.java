package com.test.demo.user.controller.port;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserUpdateDto;

public interface UserUpdateService {

    User update(long id, UserUpdateDto userUpdateDto);
}