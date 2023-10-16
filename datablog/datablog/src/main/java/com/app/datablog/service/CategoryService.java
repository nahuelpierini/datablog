package com.app.datablog.service;

import com.app.datablog.dto.CategoryDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CategoryService {

    Page<CategoryDTO> listAllCategories(int page, int size, String sortBy, String direction);
    Optional<CategoryDTO> findCategoryById(Long id);
    Optional<CategoryDTO> findCategoryByTitle(String title);
    CategoryDTO createCategory(CategoryDTO categoryDTO, Long parentId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id, Long parentId);
    void deleteCategory(Long id);

}
