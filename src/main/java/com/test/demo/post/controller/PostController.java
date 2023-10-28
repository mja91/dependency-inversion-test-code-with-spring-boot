package com.test.demo.post.controller;

import com.test.demo.user.controller.UserController;
import com.test.demo.post.controller.response.PostResponseDto;
import com.test.demo.post.domain.request.PostUpdateDto;
import com.test.demo.post.infrastructure.entity.PostEntity;
import com.test.demo.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물(posts)")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserController userController;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable long id) {
        return ResponseEntity
            .ok()
            .body(toResponse(postService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable long id, @RequestBody PostUpdateDto postUpdateDto) {
        return ResponseEntity
            .ok()
            .body(toResponse(postService.update(id, postUpdateDto)));
    }

    public PostResponseDto toResponse(PostEntity postEntity) {
        PostResponseDto PostResponse = new PostResponseDto();
        PostResponse.setId(postEntity.getId());
        PostResponse.setContent(postEntity.getContent());
        PostResponse.setCreatedAt(postEntity.getCreatedAt());
        PostResponse.setModifiedAt(postEntity.getModifiedAt());
        PostResponse.setWriter(userController.toResponse(postEntity.getWriter()));
        return PostResponse;
    }
}