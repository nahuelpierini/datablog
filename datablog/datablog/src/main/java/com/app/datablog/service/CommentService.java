package com.app.datablog.service;

import com.app.datablog.dto.CommentDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CommentService {

    Page<CommentDTO> listCommentByPost(Long postId, int page, int size);
    Optional<CommentDTO> getCommentById(Long commentId, Long postId);
    CommentDTO createComment(CommentDTO commentDTO, Long postId, Long parentCommentId, Long userId);
    CommentDTO updateComment(Long commentId, Long parentCommentId, CommentDTO commentDTO, Long postId, Long userId);
    void deleteComment(Long commentId, Long postId, Long userId);

}
