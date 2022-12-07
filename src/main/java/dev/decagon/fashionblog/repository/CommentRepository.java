package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Comment;
import dev.decagon.fashionblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
