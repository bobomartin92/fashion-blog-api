package dev.decagon.fashionblog.controller;

import dev.decagon.fashionblog.dto.CreatePostDto;
import dev.decagon.fashionblog.dto.UpdatePostDto;
import dev.decagon.fashionblog.model.Post;
import dev.decagon.fashionblog.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> createPost(@Valid @RequestBody CreatePostDto createPostDto){

        postService.createPost(createPostDto);

        return new ResponseEntity<>("Post Created Successfully", HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<Post>> getAllPosts(@RequestParam(value = "page", required = false) Integer page){
        Page<Post> posts = postService.getAllPosts(page);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path = "{post_id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long post_id){
        Post post = postService.getPostById(post_id);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PutMapping(path = "{post_id}")
    public ResponseEntity<Post> updatePostById(@PathVariable Long post_id, @Valid @RequestBody UpdatePostDto updatePostDto){
        Post post = postService.updatePostById(post_id, updatePostDto);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping(path = "{post_id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long post_id){

        postService.deletePostById(post_id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "{post_id}/like/{user_id}")
    public ResponseEntity<String> likeAndUnlikePost(@PathVariable Long post_id, @PathVariable Long user_id){

        String msg = postService.likeAndUnlikePost(post_id, user_id);

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> getAllPostsByTitleSearch(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam("q") String searchTerm){

        Page<Post> postsBySearch = postService.getAllPostsByTitleSearch(searchTerm, page);

        return new ResponseEntity<>(postsBySearch, HttpStatus.OK);
    }
}
