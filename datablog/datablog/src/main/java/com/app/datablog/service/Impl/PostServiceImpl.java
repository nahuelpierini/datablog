package com.app.datablog.service.Impl;

import com.app.datablog.dto.CategoryDTO;
import com.app.datablog.dto.PostDTO;
import com.app.datablog.dto.TagDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.models.Category;
import com.app.datablog.models.Post;
import com.app.datablog.models.Tag;
import com.app.datablog.models.User;
import com.app.datablog.repository.*;
import com.app.datablog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomPostTagRepository customPostTagRepository;

    @Override
    public Page<PostDTO> listAllPosts(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Post> postsPage = postRepository.findAll(pageable);
        Page<PostDTO> postsDTO = postsPage.map(this::mapToDTO);

        return postsDTO;
    }

    @Override
    public Page<PostDTO> listAllByCategory(Long categoryId, int page, int size, String sortBy, String direction) {

        if (!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
        }
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Post> postsPage = postRepository.findByCategoryId(categoryId, pageable);
        Page<PostDTO> postDTO = postsPage.map(this::mapToDTO);

        return postDTO;
    }

    @Override
    public Page<PostDTO> findByUserId(Long  userId, int page, int size, String sortBy, String direction) {
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Post> postsPage = postRepository.findByUserId(userId,pageable);
        Page<PostDTO> postDTO = postsPage.map(this::mapToDTO);

        return postDTO;
    }

    @Override
    public Optional<PostDTO> getPostById(Long id) {

        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.map(this::mapToDTO);
    }

    @Override
    public Optional<PostDTO> getPostByTitle(String title) {

        Optional<Post> postOptional = postRepository.findByTitle(title);
        return postOptional.map(this::mapToDTO);
    }


    @Override
    public PostDTO createPost(PostDTO postDTO, Long userId, List<Long> tagIds, Long categoryId) {

        Post newPost = mapToEntity(postDTO);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + userId + " not found"));
        newPost.setUser(user);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category with ID: " + categoryId + " not found"));

        Set<Tag> tags = new HashSet<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag with ID: " + tagIds + " not found"));
            tags.add(tag);
        }

        newPost.setCategory(category);
        newPost.setTags(tags);
        newPost.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(newPost);

        return mapToDTO(savedPost);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO, Long newUserId, List<Long> tagIds, Long newCategoryId) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID: " + id + " not found"));
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setMetaTitle(postDTO.getMetaTitle());
        existingPost.setSlug(postDTO.getSlug());
        existingPost.setContent(postDTO.getContent());
        existingPost.setSummary(postDTO.getSummary());
        existingPost.setUpdatedAt(LocalDateTime.now());

        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + newUserId + " not found"));
        existingPost.setUser(newUser);

        Category newCategory = categoryRepository.findById(newCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: " + newCategoryId + " not found"));
        existingPost.setCategory(newCategory);

        customPostTagRepository.deleteTagsByPostId(id);

        Set<Tag> newTags = new HashSet<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tag with ID: " + tagId + " not found"));
            newTags.add(tag);
        }
        existingPost.setTags(newTags);

        Post updatedPost = postRepository.save(existingPost);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)){
            throw new ResourceNotFoundException("Post with ID " + id + " not found");
        }
        postRepository.deleteById(id);

    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = new Post();

        post.setTitle(postDTO.getTitle());
        post.setMetaTitle(postDTO.getMetaTitle());
        post.setSlug(postDTO.getSlug());
        post.setContent(postDTO.getContent());
        post.setSummary(postDTO.getSummary());

        return post;
    }

    private PostDTO mapToDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setMetaTitle(post.getMetaTitle());
        postDTO.setSlug(post.getSlug());
        postDTO.setContent(post.getContent());
        postDTO.setSummary(post.getSummary());

        if (post.getUser() != null) {
            String name = post.getUser().getFirstName() + " " + post.getUser().getLastName();
            Long id = post.getUser().getId();
            postDTO.setUserName(name);
            postDTO.setUserId(id);
        }

        if (post.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(post.getUser().getId());
            categoryDTO.setTitle(post.getCategory().getTitle());
            postDTO.setCategory(categoryDTO);
        }

        if (!post.getTags().isEmpty()){
            Set<TagDTO> listTagsDTO = new HashSet<>();
            for (Tag tag : post.getTags()){
                TagDTO tagDTO = new TagDTO();
                tagDTO.setId(tag.getId());
                tagDTO.setTitle(tag.getTitle());
                listTagsDTO.add(tagDTO);
            }
            postDTO.setTags(listTagsDTO);
        }

        return postDTO;
    }
}

