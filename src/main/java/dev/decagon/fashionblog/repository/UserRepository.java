package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String userEmail);

    Optional<User> findUserByEmail(String userEmail);
}
