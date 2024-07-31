package com.earth_pointer.service;

import com.earth_pointer.dto.CommentDTO;
import com.earth_pointer.repository.CommentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommentService {
    private final CommentRepositoryImpl commentRepository;

    @Autowired
    public CommentService(CommentRepositoryImpl commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(int userId, int postId, CommentDTO comment) {
        commentRepository.addComment(userId, postId, comment);
    }

    public boolean updateComment(int commentId, String comment) {
        return commentRepository.updateComment(commentId, comment);
    }

    public boolean deleteComment(int commentId) {
        return commentRepository.deleteComment(commentId);
    }

    public CommentDTO getComment(int commentId) {
        return commentRepository.getComment(commentId);
    }

    public ArrayList<CommentDTO> getAllComments(int postId) {
        return commentRepository.getAllComments(postId);
    }
}
