package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int comment_id;
    private int post_id;
    private int user_id;
    private String comment;
    private Timestamp createdAt;
}
