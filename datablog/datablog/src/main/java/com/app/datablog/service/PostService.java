package com.app.datablog.service;

import com.app.datablog.dto.PostDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Page<PostDTO> listAllPosts(int page, int size, String sortBy, String direction);
    Page<PostDTO> listAllByCategory(Long categoryId, int page, int size, String sortBy, String direction);
    Page<PostDTO> findByUserId(Long userId, int page, int size, String sortBy, String direction);
    Optional<PostDTO> getPostById(Long id);
    Optional<PostDTO> getPostByTitle(String title);
    PostDTO createPost(PostDTO postDTO, Long userId, List<Long> tagIds, Long categoryId);
    PostDTO updatePost(Long id, PostDTO postDTO, Long newUserId, List<Long> tagIds, Long categoryId);
    void deletePost(Long id);
}

