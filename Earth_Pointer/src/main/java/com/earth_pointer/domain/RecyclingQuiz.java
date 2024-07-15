package com.earth_pointer.domain.recyclingInfos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecyclingQuiz {
    private int quizId;         
    private String question;            // 질문
    private String correctAnswer;       // 정답
    private String explanation;         // 해설
    private Timestamp createdAt;        // 퀴즈 생성 날짜 및 시간
}
