package com.earth_pointer.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private int commentId;
    private int postId;
    private int userId;
    @Size(min = 1, max = 500, message = "1글자에서 500글자까지 입력해주세요")
    private String comment;
    private Timestamp createdAt;
    private String username;
}