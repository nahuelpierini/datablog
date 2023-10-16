package com.app.datablog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 45,
            nullable = false,
            unique = true)
    private String title;

    @Column(name = "meta_title",
            nullable = false,
            unique = true)
    private String metaTitle;

    @Column(name = "slug",
            nullable = false,
            unique = true)
    private String slug;

    @Column(name = "created_at",
            columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @Column(name = "content",
            columnDefinition = "text")
    private String content;

    @Column(name = "summary",
            columnDefinition = "tinytext")
    private String summary;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "id_post"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

}