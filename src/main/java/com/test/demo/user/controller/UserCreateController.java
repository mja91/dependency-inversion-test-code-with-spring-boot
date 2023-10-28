package com.test.demo.user.controller;

import com.test.demo.user.domain.User;
import com.test.demo.user.domain.request.UserCreateDto;
import com.test.demo.user.controller.response.UserResponseDto;
import com.test.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저(users)")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCreateController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        User user = userService.create(userCreateDto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(UserResponseDto.from(user));
    }

}