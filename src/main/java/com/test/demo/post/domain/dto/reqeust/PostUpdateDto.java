package com.test.demo.post.domain.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateDto {

    private final String content;

    @Builder
    public PostUpdateDto(
            @JsonProperty("content") String content) {
        this.content = content;
    }
}
