package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Like;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository underTest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void findByPostAndUser() {
        User user = new User("Tony", "tony@email.com", "1234");
        user = userRepository.save(user);

        Post post = new Post("Test Title", "Test Description");
        post = postRepository.save(post);

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        underTest.save(like);

        Like actual = underTest.findByPostAndUser(post, user).orElseThrow();

        assertThat(actual).isEqualTo(like);
    }
}