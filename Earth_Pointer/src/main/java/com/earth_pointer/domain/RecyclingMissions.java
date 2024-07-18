package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecyclingMissions {
    private int missionId;
    private String title;           // 미션 제목
    private String description;     // 미션 설명
    private String missionType;     // 미션 종류
    private Date startDate;         // 미션 시작일
    private Date endDate;           // 미션 종료일
}
