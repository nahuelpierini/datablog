package com.app.datablog.service.Impl;

import com.app.datablog.dto.CommentDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.exceptions.UnauthorizedAccessException;
import com.app.datablog.models.Comment;
import com.app.datablog.models.Post;
import com.app.datablog.models.User;
import com.app.datablog.repository.CommentRepository;
import com.app.datablog.repository.PostRepository;
import com.app.datablog.repository.UserRepository;
import com.app.datablog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<CommentDTO> listCommentByPost(Long postId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        List<Comment> rootComments = commentPage.stream()
                .filter(comment -> comment.getParentComment() == null)
                .toList();

        List<CommentDTO> rootCommentDTOs = rootComments.stream()
                .map(this::mapToDTO)
                .toList();

        return new PageImpl<>(rootCommentDTOs, pageable, rootComments.size());
    }

    @Override
    public Optional<CommentDTO> getCommentById(Long commentId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID: " + postId + " not found"));
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        return commentOptional.map(this::mapToDTO);
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, Long postId, Long parentCommentId, Long userId) {
        if (parentCommentId == null){
            Comment newComment = mapToEntity(commentDTO);
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post with ID: " + postId + " not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));
            newComment.setPost(post);
            newComment.setUser(user);
            newComment.setPublishedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(newComment);

            return mapToDTO(savedComment);
        }else{
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Comment with ID: " + parentCommentId + " not found"));

            User user = userRepository.findById(userId)
                    .orElseThrow(()->new ResourceNotFoundException("User with ID: " + userId + " not found"));
            Comment replyComment = mapToEntity(commentDTO);
            replyComment.setPost(parentComment.getPost());
            replyComment.setUser(user);
            replyComment.setParentComment(parentComment);
            replyComment.setPublishedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(replyComment);

            return mapToDTO(savedComment);
        }
    }


    @Override
    public CommentDTO updateComment(Long commentId, Long parentCommentId, CommentDTO commentDTO, Long postId, Long userId) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID: " + commentId + " not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID: " + postId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User with ID: " + userId + " not found"));

        if (!existingComment.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this comment.");
        }

        if (parentCommentId != null) {
            if (parentCommentId.equals(commentId)){
                throw new IllegalArgumentException("A comment cannot be its own parent");
            }
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Comment with ID: " + parentCommentId + " not found"));

            existingComment.setParentComment(parentComment);
        }

        existingComment.setContent(commentDTO.getContent());
        existingComment.setPost(post);
        existingComment.setUser(user);

        Comment updatedComment = commentRepository.save(existingComment);

        return mapToDTO(updatedComment);


    }


    @Override
    public void deleteComment(Long commentId, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID: " + postId + " not found"));
        Comment comment = commentRepository
                .findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment with ID: " + commentId + " not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this comment.");
        }
        commentRepository.delete(comment);
    }

    public CommentDTO mapToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setPublishedAt(comment.getPublishedAt());

        if (comment.getChildComments() != null) {
            List<CommentDTO> childCommentDTOs = comment.getChildComments().stream()
                    .map(this::mapToDTO)
                    .toList();
            commentDTO.setChildComments(childCommentDTOs);
        }
        if (comment.getUser() != null) {
            String name = comment.getUser().getFirstName() + " " + comment.getUser().getLastName();
            Long id = comment.getUser().getId();
            commentDTO.setUserName(name);
            commentDTO.setIdUser(id);
        }
        if (comment.getPost() != null) {
            Long id = comment.getPost().getId();
            commentDTO.setIdPost(id);
        }

        return commentDTO;
    }

    public Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());

        return comment;
    }

}

