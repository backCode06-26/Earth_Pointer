package com.earth_pointer.controller;

import com.earth_pointer.dto.CommentDTO;
import com.earth_pointer.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveComment(@RequestParam String comment, @RequestParam int postId) {
        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = Integer.parseInt(session);

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setComment(comment);
        commentDTO.setUserId(userId);

        commentService.addComment(userId, postId, commentDTO);

        return ResponseEntity.ok("댓글이 성공적으로 추가되었습니다.");
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = Integer.parseInt(session);

        boolean success = commentService.deleteComment(commentId);

        if (success) {
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }
    }
    @PutMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable int commentId, @RequestParam String comment) {
        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = Integer.parseInt(session);

        boolean success = commentService.updateComment(commentId, comment);

        if (success) {
            return ResponseEntity.ok("댓글이 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }
    }
}
