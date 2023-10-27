package com.test.demo.post.service;

import com.test.demo.common.domain.exception.ResourceNotFoundException;
import com.test.demo.post.domain.dto.reqeust.PostCreateDto;
import com.test.demo.post.domain.dto.reqeust.PostUpdateDto;
import com.test.demo.post.infrastructure.entity.PostEntity;
import com.test.demo.post.infrastructure.PostJpaRepository;
import com.test.demo.user.infrastructure.entity.UserEntity;
import com.test.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final UserService userService;

    public PostEntity getById(long id) {
        return postJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity create(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getById(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postJpaRepository.save(postEntity);
    }

    public PostEntity update(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postJpaRepository.save(postEntity);
    }
}