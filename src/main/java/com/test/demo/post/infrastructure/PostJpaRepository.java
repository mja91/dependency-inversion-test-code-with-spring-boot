package com.test.demo.post.infrastructure;

import com.test.demo.post.infrastructure.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

}