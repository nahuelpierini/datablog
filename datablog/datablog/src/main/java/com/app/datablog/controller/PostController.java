package com.app.datablog.controller;

import com.app.datablog.dto.CommentDTO;
import com.app.datablog.dto.PostDTO;
import com.app.datablog.service.PostService;
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

import java.util.List;
import java.util.Optional;

@Tag(name = "6. Post", description = "Post management APIs")
@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(
            summary = "List all posts",
            description = "Get a page of posts objects specifying by page, size, sortBy, direction." +
                    " The response is a list of posts objects with post id, title, meta title," +
                    " slug, summary, user name, user id, category object, list of tags")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<Page<PostDTO>> listAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<PostDTO> posts = postService.listAllPosts(page,size,sortBy,direction);
        if (posts.isEmpty()){
            return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Retrieve a post by user id",
            description = "Get a page of posts object specifying the user id, size, page, sortBy, direction." +
                    " The response is a post object with post id, title, meta title," +
                    " slug, summary, user name, user id, category object, list of tags")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "userId", description = "List all posts by user Id", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDTO>> listAllPostByUser(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "title") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction) {
        Page<PostDTO> posts = postService.findByUserId(userId,page,size,sortBy,direction);
        if (posts.isEmpty()){
            return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Retrieve a post by category id",
            description = "Get a list of posts object specifying the category id, page, size, sortBy, direction." +
                    " The response is a post object with post id, title, meta title," +
                    " slug, summary, user name, user id, category object, list of tags")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "categoryId", description = "list all posts by categoryId", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<PostDTO>> listAllPostByCategory(@PathVariable Long categoryId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam(defaultValue = "title") String sortBy,
                                                               @RequestParam(defaultValue = "asc") String direction) {
        Page<PostDTO> posts = postService.listAllByCategory(categoryId,page,size,sortBy,direction);
        if (posts.isEmpty()){
            return new ResponseEntity<>(posts, HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Retrieve a post by id",
            description = "Get a post object specifying its id." +
                    " The response is a post object with post id, title, meta title," +
                    " slug, summary, user name, user id, category object, list of tags")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "id", description = "Search post by Id", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id){
        Optional<PostDTO> postOptional = postService.getPostById(id);
        return postOptional.map(postDTO -> new ResponseEntity<>(postDTO,HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Retrieve a post by title",
            description = "Get a post object specifying its title." +
                    " The response is a post object with post id, title, meta title," +
                    " slug, summary, user name, user id, category object, list of tags")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PostDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "title", description = "Search post by title", required = true)
    })
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/title/{title}")
    public ResponseEntity<PostDTO> getPostByTitle(@PathVariable String title){
        Optional<PostDTO> postOptional = postService.getPostByTitle(title);
        return postOptional.map(postDTO -> new ResponseEntity<>(postDTO,HttpStatus.OK))
                .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Create a post",
            description = "Create a post object by specifying mandatory its user id, category id, list of tags id " +
                    " The body require mandatory title, meta title, slug; content and summary are not mandatory. <<<Ignore fields --> Category and Tags>>> ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "userId", description = "Assign a user object to the post by userId", required = true),
            @Parameter(name = "tagIds", description = "Assign a list of tags objects to the post by tagIds", required = true),
            @Parameter(name = "categoryId", description = "Assign a category object to the post by categoryId", required = true)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @Valid @RequestBody PostDTO postDTO,
            @RequestParam Long userId,
            @RequestParam List<Long> tagIds,
            @RequestParam Long categoryId) {
        PostDTO createdPost = postService.createPost(postDTO, userId, tagIds, categoryId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a post",
            description = "Update a post object by specifying mandatory its id, user id, category id, list of tags id " +
                    " The body require mandatory title, meta title, slug; content and summary not mandatory. <<<Ignore fields --> Category and Tags>>> ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = CommentDTO.class),
                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "id", description = "Search post by id", required = true),
            @Parameter(name = "newUserId", description = "Reassign a user object to the post by newUserId", required = true),
            @Parameter(name = "newTagsId", description = "Reassign a list of tags objects to the post by newTagsId", required = true),
            @Parameter(name = "newCategoryId", description = "Reassign a category object to the post by newCategoryId", required = true)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody PostDTO postDTO,
                                              @RequestParam Long newUserId,
                                              @RequestParam List<Long> newTagsId,
                                              @RequestParam Long newCategoryId){
        PostDTO updatePost = postService.updatePost(id, postDTO, newUserId, newTagsId, newCategoryId);
        return ResponseEntity.ok(updatePost);
    }

    @Operation(
            summary = "Delete a post",
            description = "Delete a post object by specifying its id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @Parameters({
            @Parameter(name = "id", description = "Search post by Id", required = true)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
