package com.app.datablog.repository;

import com.app.datablog.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findAllTagsByPostsId(Long postId, Sort sort);
    Page<Tag> findAll(Pageable pageable);
}