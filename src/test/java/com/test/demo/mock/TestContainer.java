package com.test.demo.mock;

import com.test.demo.common.service.port.ClockHolder;
import com.test.demo.common.service.port.UuidHolder;
import com.test.demo.user.controller.UserController;
import com.test.demo.user.controller.UserCreateController;
import com.test.demo.user.controller.port.*;
import com.test.demo.user.service.CertificationServiceImpl;
import com.test.demo.user.service.UserServiceImpl;
import com.test.demo.user.service.port.MailSender;
import com.test.demo.user.service.port.UserRepository;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final CertificationServiceImpl certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;

    public TestContainer(UuidHolder uuidHolder, ClockHolder clockHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.certificationService = new CertificationServiceImpl(this.mailSender);
        UserServiceImpl userServiceImpl = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationServiceImpl(this.certificationService)
                .build();
        this.userReadService = userServiceImpl;
        this.userCreateService = userServiceImpl;
        this.userUpdateService = userServiceImpl;
        this.authenticationService = userServiceImpl;
        this.userController = UserController.builder()
                .userReadService(this.userReadService)
                .userCreateService(this.userCreateService)
                .userUpdateService(this.userUpdateService)
                .authenticationService(this.authenticationService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(this.userCreateService)
                .build();
    }
}
