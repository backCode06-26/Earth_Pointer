package com.earth_pointer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int postId;
    private int userId;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 5000, message = "내용은 최대 5000자까지 가능합니다.")
    private String content;

    private Timestamp createdAt;
}
