package com.app.datablog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title",
            length = 100,
            unique = true,
            nullable = false)
    private String title;

    @Column(name = "meta_title",
            unique = true,
            nullable = false)
    private String metaTitle;

    @Column(name = "slug",
            unique = true,
            nullable = false)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory",
            cascade = CascadeType.PERSIST)
    private List<Category> childCategories;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.PERSIST)
    private Set<Post> posts = new HashSet<>();
}
