package dev.decagon.fashionblog.repository;

import dev.decagon.fashionblog.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void testFindUserByEmailTrue() {
        User user = new User("Tony", "tony@email.com", "1234");

        underTest.save(user);

        boolean actual = underTest.existsByEmail(user.getEmail());

        assertThat(actual).isTrue();
    }

    @Test
    void testFindUserByEmailFalse() {
        User user = new User("Tony", "tony@email.com", "1234");

        boolean actual = underTest.existsByEmail(user.getEmail());

        assertThat(actual).isFalse();
    }

    @Test
    void textFindUserByEmail() {
        User user = new User("Tony", "tony@email.com", "1234");

        underTest.save(user);

        User actual = underTest.findUserByEmail(user.getEmail()).orElseThrow();

        assertThat(actual).isEqualTo(user);
    }
}