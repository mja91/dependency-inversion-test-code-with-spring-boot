package com.test.demo.user.controller.port;

import com.test.demo.user.domain.User;

public interface UserReadService {

    User getByEmail(String email);

    User getById(long id);
}
