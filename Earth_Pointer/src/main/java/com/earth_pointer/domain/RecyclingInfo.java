package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecyclingInfo { // 재활용 정보
    private int infoId;
    private String title;
    private String content;
    private String category;
    private int authorId;           // 작성자 id (= userId)
    private Timestamp createdAt;    // 작성 날짜 및 시간
}
