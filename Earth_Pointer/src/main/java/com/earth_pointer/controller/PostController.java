package com.earth_pointer.controller;

import com.earth_pointer.domain.Post;
import com.earth_pointer.dto.PostDTO;
import com.earth_pointer.service.PostService;
import com.earth_pointer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
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

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.postSave(email, postDTO);
        return "redirect:/post/posts";
    }

    // 게시글 세부사항
    @GetMapping("/{postId}")
    public String getPostDetails(@PathVariable int postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "post/details";
    }

    // 게시글 수정
    @GetMapping("/edit/{postId}")
    public String showEdit(@PathVariable int postId, Model model) {
        if (!isUserAuthorized(postId)) {
            return "redirect:/post/posts";
        }

        Post post = postService.getPostById(postId);
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
    @GetMapping("/delete/{postId}")
    public String showDelete(@PathVariable int postId, Model model) {
        if (!isUserAuthorized(postId)) {
            return "redirect:/post/posts";
        }

        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "post/delete";
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable int postId) {
        if (!isUserAuthorized(postId)) {
            return "redirect:/post/posts";
        }

        postService.postDelete(postId);
        return "redirect:/post/posts";
    }

    // 게시글 페이징
    @GetMapping("/posts")
    public String getAllPostsWithPaginating(@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model) {
        int totalPosts = postService.getTotalPosts();
        int page = (int) Math.ceil((double) totalPosts / 10);
        List<Post> posts = postService.getAllPostsWithPagination(pageNumber);
        model.addAttribute("page", page);
        model.addAttribute("posts", posts);
        return "post/posts";
    }

    // 사용자 권한 체크
    private boolean isUserAuthorized(int postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = userService.findUserIdByEmail(email);
        Post post = postService.getPostById(postId);
        return post != null && post.getUserId() == userId;
    }
}
