package com.test.demo.user.controller;

import com.test.demo.user.controller.port.*;
import com.test.demo.user.domain.User;
import com.test.demo.user.controller.response.MyProfileResponseDto;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.domain.request.UserUpdateDto;
import com.test.demo.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "유저(users)")
@Builder
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserReadService userReadService;
    private final UserCreateService userCreateService;
    private final UserUpdateService userUpdateService;
    private final AuthenticationService authenticationService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponseDto.from(userReadService.getById(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        authenticationService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDto> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져온다.
    ) {
        User user = userReadService.getByEmail(email);
        authenticationService.login(user.getId());
        return ResponseEntity
            .ok()
            .body(MyProfileResponseDto.from(user));
    }

    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<MyProfileResponseDto> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져온다.
        @RequestBody UserUpdateDto userUpdateDto
    ) {
        User user = userReadService.getByEmail(email);
        user = userUpdateService.update(user.getId(), userUpdateDto);
        return ResponseEntity
            .ok()
            .body(MyProfileResponseDto.from(user));
    }
}