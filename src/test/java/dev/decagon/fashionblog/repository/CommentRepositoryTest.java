package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Comment;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository underTest;
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
    void testFindAllCommentByPost() {
        User user = new User("Tony", "tony@email.com", "1234");
        user = userRepository.save(user);

        Post post = new Post("Test Title", "Test Description");
        post = postRepository.save(post);

        Comment com1 = new Comment("Comment1", user, post);
        Comment com2 = new Comment("Comment2", user, post);
        underTest.save(com1);
        underTest.save(com2);

        List<Comment> actual = underTest.findAllByPost(post);

        assertThat(actual.size()).isEqualTo(2);
    }
}