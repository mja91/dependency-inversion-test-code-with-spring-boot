package com.test.demo.user.controller;

import com.test.demo.user.domain.dto.response.MyProfileResponseDto;
import com.test.demo.user.domain.dto.response.UserResponseDto;
import com.test.demo.user.domain.dto.request.UserUpdateDto;
import com.test.demo.user.infrastructure.entity.UserEntity;
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
            .body(toResponse(userService.getById(id)));
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
        UserEntity userEntity = userService.getByEmail(email);
        userService.login(userEntity.getId());
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(userEntity));
    }

    @PutMapping("/me")
    @Parameter(in = ParameterIn.HEADER, name = "EMAIL")
    public ResponseEntity<MyProfileResponseDto> updateMyInfo(
        @Parameter(name = "EMAIL", in = ParameterIn.HEADER)
        @RequestHeader("EMAIL") String email, // 일반적으로 스프링 시큐리티를 사용한다면 UserPrincipal 에서 가져온다.
        @RequestBody UserUpdateDto userUpdateDto
    ) {
        UserEntity userEntity = userService.getByEmail(email);
        userEntity = userService.update(userEntity.getId(), userUpdateDto);
        return ResponseEntity
            .ok()
            .body(toMyProfileResponse(userEntity));
    }

    public UserResponseDto toResponse(UserEntity userEntity) {
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(userEntity.getId());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setNickname(userEntity.getNickname());
        userResponse.setStatus(userEntity.getStatus());
        userResponse.setLastLoginAt(userEntity.getLastLoginAt());
        return userResponse;
    }

    public MyProfileResponseDto toMyProfileResponse(UserEntity userEntity) {
        MyProfileResponseDto myProfileResponse = new MyProfileResponseDto();
        myProfileResponse.setId(userEntity.getId());
        myProfileResponse.setEmail(userEntity.getEmail());
        myProfileResponse.setNickname(userEntity.getNickname());
        myProfileResponse.setStatus(userEntity.getStatus());
        myProfileResponse.setAddress(userEntity.getAddress());
        myProfileResponse.setLastLoginAt(userEntity.getLastLoginAt());
        return myProfileResponse;
    }
}