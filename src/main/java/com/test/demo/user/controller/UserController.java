package com.test.demo.user.controller;

import com.test.demo.user.domain.User;
import com.test.demo.user.controller.response.MyProfileResponseDto;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.domain.request.UserUpdateDto;
import com.test.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "유저(users)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(UserResponseDto.from(userService.getById(id)));
    }

    @GetMapping("/{id}/verify")
    public ResponseEntity<Void> verifyEmail(
        @PathVariable long id,
        @RequestParam String certificationCode) {
        userService.verifyEmail(id, certificationCode);
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://localhost:3000"))
            .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyProfileResponseDto> getMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져온다.
    ) {
        User user = userService.getByEmail(email);
        userService.login(user.getId());
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
        User user = userService.getByEmail(email);
        user = userService.update(user.getId(), userUpdateDto);
        return ResponseEntity
            .ok()
            .body(MyProfileResponseDto.from(user));
    }
}