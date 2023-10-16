package com.app.datablog.repository;

import com.app.datablog.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByTitle(String title);
    Page<Category> findAll(Pageable pageable);

}