package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Like;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
}
