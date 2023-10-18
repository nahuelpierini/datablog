package com.app.datablog.controller;

import com.app.datablog.dto.CommentDTO;
import com.app.datablog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "7. Comment", description = "Comment management APIs")
@RestController
@RequestMapping("/api/comment")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(
            summary = "List all comments by post",
            description = "Get a page of comments objects specifying post Id, size, page." +
                    " The response is a list of comments objects with  comment id, published date, content," +
                    " user name, user id, post id, child comment object.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "postId", description = "Search comments by postId", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<Page<CommentDTO>> listAllCommentsByPost(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentDTO> comments = commentService.listCommentByPost(postId, page, size);
        if (comments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Retrieve a comment by id",
            description = "Get a comment object by specifying its comment id." +
                    " The response is comment object with  comment id, published date, content," +
                    " user name, user id, post id, child comment object.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "commentId", description = "Search comment by commentId", required = true),
            @Parameter(name = "postId", description = "Search comment by postId", required = true),
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId,
                                                     @RequestParam Long postId) {
        Optional<CommentDTO> commentOptional = commentService.getCommentById(commentId, postId);
        return commentOptional.map(postDTO -> new ResponseEntity<>(postDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create a comment",
            description = "Create a comment object by specifying mandatory its post id, user id and parent id not mandatory" +
                    " (without parent id is  a comment, with parent id is a reply). " +
                    " The body require content")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "postId", description = "Assign a post object to the comment by postId", required = true),
            @Parameter(name = "parentCommentId", description = "Assign a parent comment object to the comment by parentCommentId", required = false),
            @Parameter(name = "userId", description = "Assign a user object to the comment by userId", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestParam Long postId,
                                                    @RequestParam(required = false) Long parentCommentId,
                                                    @Valid @RequestBody CommentDTO commentDTO,
                                                    @RequestParam Long userId){
        CommentDTO createdComment = commentService.createComment(commentDTO, postId, parentCommentId,userId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Update a comment",
            description = "Update a comment object by specifying mandatory its comment id, post id, user id and parent id not mandatory" +
                    " (without parent id is  a comment, with parent id is a reply). " +
                    " The body require content")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "commentId", description = "Search a comment by commentId", required = true),
            @Parameter(name = "postId", description = "Reassign a post object to the comment by postId", required = true),
            @Parameter(name = "parentCommentId", description = "Reassign a parent comment object to the comment by parentCommentId", required = false),
            @Parameter(name = "userId", description = "Reassign a user object to the comment by userId", required = true)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "commentId") Long commentId,
                                                    @RequestParam(required = false) Long parentCommentId,
                                                    @Valid @RequestBody CommentDTO commentDTO,
                                                    @RequestParam Long postId,
                                                    @RequestParam Long userId) {
        CommentDTO updateComment = commentService.updateComment(commentId, parentCommentId, commentDTO, postId, userId);
        return ResponseEntity.ok(updateComment);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment object by specifying its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "commentId", description = "Search comment by commentId", required = true),
            @Parameter(name = "postId", description = "Search comment by postId", required = true),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @RequestParam  Long postId,
                                                @RequestParam Long userId){
        commentService.deleteComment(commentId, postId,userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
