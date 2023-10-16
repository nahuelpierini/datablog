package com.app.datablog.service.Impl;

import com.app.datablog.dto.CategoryDTO;
import com.app.datablog.exceptions.ResourceNotFoundException;
import com.app.datablog.models.Category;
import com.app.datablog.models.Post;
import com.app.datablog.repository.CategoryRepository;
import com.app.datablog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDTO> listAllCategories(int page, int size, String sortBy, String direction) {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortBy);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> rootComments = categoryPage.stream()
                .filter(category -> category.getParentCategory() == null)
                .toList();

        List<CategoryDTO> rootCategoryDTOs = rootComments.stream()
                .map(this::mapToDTO)
                .toList();

        return new PageImpl<>(rootCategoryDTOs, pageable, rootComments.size());
    }

    @Override
    public Optional<CategoryDTO> findCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        return categoryOptional.map(this::mapToDTO);
    }

    @Override
    public Optional<CategoryDTO> findCategoryByTitle(String title) {
        Optional<Category> categoryOptional = categoryRepository.findByTitle(title);

        return categoryOptional.map(this::mapToDTO);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO, Long parentId) {
        if (parentId == null){

            Category newCategory = mapToEntity(categoryDTO);
            Category savedCategory = categoryRepository.save(newCategory);
            return mapToDTO(savedCategory);
        }else{

            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(()->new ResourceNotFoundException("Category with ID: " + parentId + " not found"));

            Category newSubcategory = mapToEntity(categoryDTO);
            newSubcategory.setParentCategory(parentCategory);
            Category savedSubcategory = categoryRepository.save(newSubcategory);

            return mapToDTO(savedSubcategory);
        }
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id, Long parentId) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID: " + id + " not found"));

        if (parentId != null) {
            if (parentId.equals(id)) {
                throw new IllegalArgumentException("A category cannot be its own parent");
            }

            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Category with ID: " + id + " not found"));

            existingCategory.setParentCategory(parentCategory);
        }

        existingCategory.setTitle(categoryDTO.getTitle());
        existingCategory.setMetaTitle(categoryDTO.getMetaTitle());
        existingCategory.setSlug(categoryDTO.getSlug());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return mapToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()){
            throw new ResourceNotFoundException("Category with ID: " + id + " not found");
        }

        Category category = optionalCategory.get();
        if (!category.getChildCategories().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a category with child categories.");
        }
        for (Post post : category.getPosts()){
            post.setCategory(null);
        }

        categoryRepository.deleteById(id);
    }


    private CategoryDTO mapToDTO(Category category){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setTitle(category.getTitle());
        categoryDTO.setMetaTitle(category.getMetaTitle());
        categoryDTO.setSlug(category.getSlug());

        if (category.getChildCategories() != null){
            List<CategoryDTO> childCategoryDTOs = category.getChildCategories().stream().map(this::mapToDTO).toList();
            categoryDTO.setChildCategories(childCategoryDTOs);
        }
        return categoryDTO;
    }

    private Category mapToEntity(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());
        category.setMetaTitle(categoryDTO.getMetaTitle());
        category.setSlug(categoryDTO.getSlug());

        return category;
    }


}
