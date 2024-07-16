package com.earth_pointer.service;

import com.earth_pointer.domain.Post;
import com.earth_pointer.dto.PostDTO;
import com.earth_pointer.repository.PostRepositoryImpl;
import com.earth_pointer.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepositoryImpl postRepository;
    private final UserRepositoryImpl userRepository;

    @Autowired
    public PostService(PostRepositoryImpl postRepository, UserRepositoryImpl userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void postSave(String email, PostDTO postDTO) {
        postRepository.addPost(email, postDTO);
    }

    public void postUpdate(int postId, PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        postRepository.updatePost(postId, post);
    }

    public void postDelete(int postId) {
        postRepository.deletePost(postId);
    }

    public ArrayList<Post> getAllPosts(String email) {
        return postRepository.getAllPosts(email);
    }

    public ArrayList<Post> getAllPostsWithPagination(int page) {
        return postRepository.getAllPostsWithPagination(page);
    }

    public int getTotalPosts() {
        return postRepository.countPost();
    }

    public Post getPostById(int postId) {
        return postRepository.getPost(postId);
    }
}
