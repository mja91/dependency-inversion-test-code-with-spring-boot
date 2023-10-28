package com.test.demo.post.domain;

import com.test.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Post {

    private Long id;

    private String content;

    private Long createdAt;

    private Long modifiedAt;

    private User writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, User writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }
}
