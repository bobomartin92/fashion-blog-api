package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void searchAllByTitleContainsIgnoreCase() {
        //given
        Post post1 = new Post("Test 1", "Test Description");
        Post post2 = new Post("Test Title 2", "Test Description");
        Post post3 = new Post("Test 3", "Test Description");
        Post post4 = new Post("Test Title 4", "Test Description");

        int page = 0;
        PageRequest pageRequest = PageRequest.of(
                page, 10);

        underTest.saveAll(List.of(post1, post2, post3, post4));

        //when
        Page<Post> actual = underTest.searchAllByTitleContainsIgnoreCase("title", pageRequest);

        assertThat(actual).hasSize(2);
    }
}