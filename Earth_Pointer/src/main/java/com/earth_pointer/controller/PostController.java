package com.earth_pointer.controller;

import com.earth_pointer.domain.Post;
import com.earth_pointer.dto.CommentDTO;
import com.earth_pointer.dto.PostDTO;
import com.earth_pointer.service.CommentService;
import com.earth_pointer.service.PostService;
import com.earth_pointer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    // 게시글 작성
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("post", new PostDTO());
        return "post/create";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDTO postDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "post/create";
        }

        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = Integer.parseInt(session);
        postService.postSave(userId, postDTO);
        return "redirect:/post";
    }

    // 게시글 세부사항
    @GetMapping("/{postId}")
    public String getPostDetails(@PathVariable int postId, Model model) {
        PostDTO post = postService.getPostById(postId);
        ArrayList<CommentDTO> comments = commentService.getAllComments(postId);
        String session = SecurityContextHolder.getContext().getAuthentication().getName();

        int userId = Integer.parseInt(session);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("userId", userId);
        return "post/details";
    }

    // 게시글 수정
    @GetMapping("/edit/{postId}")
    public String showEdit(@PathVariable int postId, Model model) {
        if (!isUserAuthorized(postId)) {
            return "redirect:/post/posts";
        }

        PostDTO post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "post/edit";
    }

    @PostMapping("/edit/{postId}")
    public String updateEdit(@PathVariable int postId, @Valid @ModelAttribute("post") PostDTO postDTO,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postDTO);
            return "post/edit";
        }

        postService.postUpdate(postId, postDTO);
        return "redirect:/post/posts";
    }

    // 게시글 삭제
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePostAjax(@PathVariable int postId) {
        if(!isUserAuthorized(postId)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        postService.postDelete(postId);
        return new ResponseEntity<>("Post deleted", HttpStatus.OK);
    }

    // 게시글 페이징
    @GetMapping("")
    public String getAllPostsWithPaginating(@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model) {
        int totalPosts = postService.getTotalPosts();
        int page = (int) Math.ceil((double) totalPosts / 10);
        System.out.println(page);
        List<Post> posts = postService.getAllPostsWithPagination(pageNumber);
        model.addAttribute("page", page);
        model.addAttribute("posts", posts);
        return "post/posts";
    }

    // 사용자 권한 체크
    private boolean isUserAuthorized(int postId) {
        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(postId);
        PostDTO post = postService.getPostById(postId);

        int userId = Integer.parseInt(session);
        int postUserId = post.getUserId();

        return postUserId == userId;
    }
}
