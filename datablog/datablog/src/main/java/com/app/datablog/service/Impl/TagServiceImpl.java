package com.app.datablog.service.Impl;

import com.app.datablog.dto.TagDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.models.Tag;
import com.app.datablog.repository.CustomPostTagRepository;
import com.app.datablog.repository.PostRepository;
import com.app.datablog.repository.TagRepository;
import com.app.datablog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CustomPostTagRepository customPostTagRepository;

    @Override
    public Page<TagDTO> listAllTags(int page, int size, String sortBy, String direction) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Tag> tagsPage = tagRepository.findAll(pageable);
        Page<TagDTO> tagDTOs = tagsPage.map(this::mapToDTO);

        return tagDTOs;
    }

    @Override
    public List<TagDTO> listAllTagsByPost(Long postId, String sortBy, String direction) {
        Sort sort;
        if ("title".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "title");
        } else if ("id".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "id");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "title");
        }
        if ("desc".equalsIgnoreCase(direction)) {
            sort = sort.descending();
        }

        List<Tag> tags = tagRepository.findAllTagsByPostsId(postId, sort);
        List<TagDTO> tagDTOs = tags.stream().map(this::mapToDTO).collect(Collectors.toList());

        return tagDTOs;
    }

    @Override
    public Optional<TagDTO> getTagById(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        return tagOptional.map(this::mapToDTO);
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag newTag = mapToEntity(tagDTO);
        Tag savedTag = tagRepository.save(newTag);
        return mapToDTO(savedTag);
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(()->  new ResourceNotFoundException("Tag with ID " + id + " not found"));

        existingTag.setTitle(tagDTO.getTitle());
        existingTag.setMetaTitle(tagDTO.getMetaTitle());
        existingTag.setSlug(tagDTO.getSlug());
        Tag updatedTag = tagRepository.save(existingTag);

        return mapToDTO(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long tagId) {
        customPostTagRepository.deleteTagReferencesByTagId(tagId);

        tagRepository.deleteById(tagId);
    }


    private Tag mapToEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setTitle(tagDTO.getTitle());
        tag.setMetaTitle(tagDTO.getMetaTitle());
        tag.setSlug(tagDTO.getSlug());
        return tag;
    }

    private TagDTO mapToDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setTitle(tag.getTitle());
        tagDTO.setMetaTitle(tag.getMetaTitle());
        tagDTO.setSlug(tag.getSlug());

        return tagDTO;
    }

}
