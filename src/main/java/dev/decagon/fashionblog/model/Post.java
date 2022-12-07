package dev.decagon.fashionblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "Post")
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "post",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    @JsonIgnore
    private final List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    public void setUpdatedAt(){
        updatedAt= LocalDateTime.now();
    }

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
