package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> searchAllByTitleContainsIgnoreCase(String title, PageRequest pageRequest);
}
