package com.test.demo.post.service;

import com.test.demo.common.domain.exception.ResourceNotFoundException;
import com.test.demo.post.domain.request.PostCreateDto;
import com.test.demo.post.domain.request.PostUpdateDto;
import com.test.demo.post.infrastructure.entity.PostEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql("/sql/post-service-test-data.sql")
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getById를_통해_게시물을_조회할_수_있다() {
        // given
        // when
        PostEntity postEntity = postService.getById(1);

        // then
        assertThat(postEntity.getId()).isEqualTo(1);
    }

    @Test
    void getById를_통해_존재하지_않는_게시물을_조회하면_예외를_반환한다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            PostEntity postEntity = postService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create를_통해_게시물을_생성할_수_있다() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .content("test content")
                .writerId(2L)
                .build();

        // when
        PostEntity postEntity = postService.create(postCreateDto);

        // then
        assertThat(postEntity.getId()).isNotNull();
        assertThat(postEntity.getContent()).isNotNull();
        assertThat(postEntity.getCreatedAt()).isGreaterThan(0L);
    }

    @Test
    void update를_통해_게시물을_수정할_수_있다() {
        // given
        PostEntity updateTargetPostEntity = postService.getById(1);
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("updated test content")
                .build();

        // when
        PostEntity postEntity = postService.update(updateTargetPostEntity.getId(), postUpdateDto);

        // then
        assertThat(postEntity.getContent()).isEqualTo(postUpdateDto.getContent());
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0L);
    }



}
