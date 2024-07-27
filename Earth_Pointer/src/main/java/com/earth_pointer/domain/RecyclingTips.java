package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecyclingTips {
    private int tipId;
    private String title;           // 팁 제목
    private String description;     // 팁 설명
    private String tipType;         // 팁 종류
    private Date createdAt;         // 팁 생성일
}
