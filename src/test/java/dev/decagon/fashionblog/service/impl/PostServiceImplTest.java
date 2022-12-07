package dev.decagon.fashionblog.service.impl;

import dev.decagon.fashionblog.dto.CreatePostDto;
import dev.decagon.fashionblog.dto.UpdatePostDto;
import dev.decagon.fashionblog.exception.NotFoundException;
import dev.decagon.fashionblog.exception.UnauthorizedException;
import dev.decagon.fashionblog.model.Like;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.model.User;
import dev.decagon.fashionblog.repository.LikeRepository;
import dev.decagon.fashionblog.repository.PostRepository;
import dev.decagon.fashionblog.repository.UserRepository;
import dev.decagon.fashionblog.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;

    private PostService underTest;

    @Mock
    private ModelMapper mapper;


    @BeforeEach
    void setUp() {
        underTest = new PostServiceImpl(postRepository, userRepository,likeRepository, mapper);
    }

    @Test
    void testCreatePost() {
        //given
        CreatePostDto postDto = new CreatePostDto("Title", "Description for testing", "ADMIN");
        Post post = new Post("Title", "Description for testing");
        given(mapper.map(postDto, Post.class)).willReturn(post);

        //when
        underTest.createPost(postDto);

        //then
        ArgumentCaptor<Post> studentArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(studentArgumentCaptor.capture());

        Post capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(post);
    }

    @Test
    void testCreatePostUnauthorizedUser() {
        //given
        CreatePostDto postDto = new CreatePostDto("Title", "Description for testing", "MEMBER");

        //when
        assertThatThrownBy(() -> underTest.createPost(postDto))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("Unauthorized User Action");

        //then
        verify(postRepository, never()).save(any());
    }

    @Test
    void getAllPosts() {

        //given
        int page = 0;
        PageRequest pageRequest = PageRequest.of(
                page, 10, Sort.by("createdAt").descending());

        //when
        underTest.getAllPosts(page);

        //then
        verify(postRepository).findAll(pageRequest);
    }

    @Test
    void getPost() {
        Post post = new Post("Title", "Description for testing");
        post.setId(1L);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));

        underTest.getPostById(post.getId());

        verify(postRepository).findById(any());
    }

    @Test
    void getPostNotFound() {
        Post post = new Post("Title", "Description for testing");
        post.setId(100L);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getPostById(post.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Post with ID: " + post.getId() + " Not Found");
    }

    @Test
    void updatePost() {
        UpdatePostDto postDto = new UpdatePostDto("Updated Title", "Description for testing");
        Post post = new Post("Updated Title", "Description for testing");
        post.setId(1L);

        given(postRepository.findById(post.getId()))
                .willReturn(Optional.of(post));

        Post actual = underTest.updatePostById(post.getId(), postDto);

        assertThat(actual).isEqualTo(post);
    }

    @Test
    void testDeletePost() {
        Long post_id = 1L;

        given(postRepository.existsById(post_id)).willReturn(true);
        underTest.deletePostById(post_id);

        verify(postRepository).deleteById(post_id);
    }

    @Test
    void testDeletePostNotFound() {
        Long post_id = 100L;

        given(postRepository.existsById(post_id)).willReturn(false);

        assertThatThrownBy(() -> underTest.deletePostById(post_id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Post with ID: " + post_id + " Not Found");

        verify(postRepository, never()).deleteById(post_id);
    }

    @Test
    void testLikePost() {
        Post post = new Post("Title", "Description for testing");
        post.setId(1L);

        User user = new User("Tom", "tom@email.com", "1234");
        user.setId(2L);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));


        underTest.likeAndUnlikePost(post.getId(), user.getId());

        verify(likeRepository).save(any());
    }

    @Test
    void testUnlikePost() {
        Post post = new Post("Title", "Description for testing");
        post.setId(1L);

        User user = new User("Tom", "tom@email.com", "1234");
        user.setId(2L);

        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        given(likeRepository.findByPostAndUser(post, user)).willReturn(Optional.of(like));

        underTest.likeAndUnlikePost(post.getId(), user.getId());

        verify(likeRepository).delete(any());
    }

    @Test
    void testSearchPostByTitle(){

        //given
        int page = 0;
        PageRequest pageRequest = PageRequest.of(
                page, 10);

        //when
        underTest.getAllPostsByTitleSearch("title", page);

        //then
        verify(postRepository).searchAllByTitleContainsIgnoreCase("title", pageRequest);
    }
}