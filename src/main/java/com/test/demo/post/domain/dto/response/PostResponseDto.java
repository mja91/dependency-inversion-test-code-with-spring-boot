package com.test.demo.post.domain.dto.response;

import com.test.demo.user.domain.dto.response.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponseDto writer;
}
