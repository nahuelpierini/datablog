package com.app.datablog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

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

    @ManyToMany(mappedBy = "tags",
            cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();

}

