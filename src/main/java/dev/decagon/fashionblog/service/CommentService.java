package dev.decagon.fashionblog.service;

import dev.decagon.fashionblog.dto.CreateCommentDto;
import dev.decagon.fashionblog.model.Comment;

import java.util.List;

public interface CommentService {
    void createComment(Long post_id, CreateCommentDto commentDto);

    List<Comment> getAllPostComments(Long post_id);
}
