package com.app.datablog.service;

import com.app.datablog.dto.TagDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Page<TagDTO> listAllTags(int page, int size, String sortBy, String direction);
    List<TagDTO> listAllTagsByPost(Long postId, String sortBy, String direction);
    Optional<TagDTO> getTagById(Long id);
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(Long id, TagDTO tagDTO);
    void deleteTag(Long id);

}
