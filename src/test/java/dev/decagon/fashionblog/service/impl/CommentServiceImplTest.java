package dev.decagon.fashionblog.service.impl;

import dev.decagon.fashionblog.dto.CreateCommentDto;
import dev.decagon.fashionblog.exception.NotFoundException;
import dev.decagon.fashionblog.model.Comment;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.model.User;
import dev.decagon.fashionblog.repository.CommentRepository;
import dev.decagon.fashionblog.repository.PostRepository;
import dev.decagon.fashionblog.repository.UserRepository;
import dev.decagon.fashionblog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private CommentService underTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        underTest = new CommentServiceImpl(commentRepository, postRepository, userRepository);
    }

    @Test
    void createComment() {
        //given
        User user = new User("Tom", "tom@email.com", "1234");
        user.setId(2L);

        Post post = new Post("Title", "Description for testing");
        post.setId(5L);

        Comment comment = new Comment("Comment", user, post);

        CreateCommentDto commentDto = new CreateCommentDto("Comment", 2L);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        //when
        underTest.createComment(5L, commentDto);

        //then
        ArgumentCaptor<Comment> studentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        verify(commentRepository).save(studentArgumentCaptor.capture());

        Comment capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(comment);
    }

    @Test
    void createCommentPostNotFound() {
        //given
        User user = new User("Tom", "tom@email.com", "1234");
        user.setId(2L);

        Post post = new Post("Title", "Description for testing");
        post.setId(5L);

        CreateCommentDto commentDto = new CreateCommentDto("Comment", 2L);

        given(postRepository.findById(post.getId())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> underTest.createComment(5L, commentDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Post with ID: " + 5 + " Not Found");

        //then
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createCommentUserNotFound() {
        //given
        User user = new User("Tom", "tom@email.com", "1234");
        user.setId(2L);

        Post post = new Post("Title", "Description for testing");
        post.setId(5L);

        CreateCommentDto commentDto = new CreateCommentDto("Comment", 2L);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> underTest.createComment(5L, commentDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with ID: " + 2 + " Not Found");

        //then
        verify(commentRepository, never()).save(any());
    }

    @Test
    void getAllPostComments() {
        //given
        Post post = new Post("Title", "Description for testing");
        post.setId(5L);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));

        //when
        underTest.getAllPostComments(5L);

        //then
        verify(commentRepository).findAllByPost(post);
    }
}