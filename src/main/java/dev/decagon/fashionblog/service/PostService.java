package dev.decagon.fashionblog.service;

import dev.decagon.fashionblog.dto.CreatePostDto;
import dev.decagon.fashionblog.dto.UpdatePostDto;
import dev.decagon.fashionblog.model.Post;
import org.springframework.data.domain.Page;

public interface PostService {
    void createPost(CreatePostDto createPostDto);

    Page<Post> getAllPosts(Integer page);

    Post getPostById(Long post_id);

    Post updatePostById(Long post_id, UpdatePostDto updatePostDto);

    void deletePostById(Long post_id);

    String likeAndUnlikePost(Long post_id, Long user_id);

    Page<Post> getAllPostsByTitleSearch(String searchTerm, Integer page);
}
