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
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    private final ModelMapper mapper;


    @Override
    public void createPost(CreatePostDto createPostDto) {
        String role = createPostDto.getRole();
        if (!role.equalsIgnoreCase("ADMIN")){
            throw new UnauthorizedException("Unauthorized User Action");
        }

        Post post = mapper.map(createPostDto, Post.class);

        postRepository.save(post);
    }

    @Override
    public Page<Post> getAllPosts(Integer page) {
        int pageNumber = (page != null) ? page : 0;

        PageRequest pageRequest = PageRequest.of(
                pageNumber, 10, Sort.by("createdAt").descending()
        );
        return postRepository.findAll(pageRequest);
    }

    @Override
    public Post getPostById(Long post_id) {

        return postRepository.findById(post_id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Post with ID: " + post_id + " Not Found");
                });
    }

    @Override
    @Transactional
    public Post updatePostById(Long post_id, UpdatePostDto updatePostDto) {
        String title = updatePostDto.getTitle();
        String description = updatePostDto.getDescription();

        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Post with ID: " + post_id + " Not Found");
                });

        if(title != null && title.length() > 0 && !title.equals(post.getTitle())){
            post.setTitle(title);
            post.setUpdatedAt(LocalDateTime.now());
        }

        if(description != null && description.length() > 0 && !description.equals(post.getDescription())){
            post.setDescription(description);
            post.setUpdatedAt(LocalDateTime.now());
        }

        return post;
    }

    @Override
    public void deletePostById(Long post_id) {
        boolean exists = postRepository.existsById(post_id);

        if(!exists){
            throw new NotFoundException("Post with ID: " + post_id + " Not Found");
        }

        postRepository.deleteById(post_id);
    }

    @Override
    public String likeAndUnlikePost(Long post_id, Long user_id) {

        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Post with ID: " + post_id + " Not Found");
                });

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> {
                    throw new NotFoundException("User with ID: " + user_id + " Not Found");
                });

        Optional<Like> liked = likeRepository.findByPostAndUser(post, user);

        if (liked.isPresent()) {
            likeRepository.delete(liked.get());
            return "User Unliked Post";
        }

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        likeRepository.save(like);

        return "User liked Post";
    }

    @Override
    public Page<Post> getAllPostsByTitleSearch(String searchTerm, Integer page) {

        int pageNumber = (page != null) ? page : 0;

        PageRequest pageRequest = PageRequest.of(pageNumber, 10);

        return postRepository.searchAllByTitleContainsIgnoreCase(searchTerm, pageRequest);
    }
}
