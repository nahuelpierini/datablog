package com.app.datablog.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name",
            length = 45,
            nullable = false)
    private String firstName;

    @Column(name = "last_name",
            length = 45,
            nullable = false)
    private String lastName;

    @Column(name = "email",
            length = 70,
            nullable = false,
            unique = true)
    private String email;

    @Column(name = "password",
            length = 200,
            nullable = false)
    private String password;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}
