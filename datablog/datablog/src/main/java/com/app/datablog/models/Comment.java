package com.app.datablog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "published_at",
            columnDefinition = "datetime")
    private LocalDateTime publishedAt;

    @Column(name = "content",
            columnDefinition = "text")
    private String content;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment",
            cascade = CascadeType.PERSIST)
    private List<Comment> childComments;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}

