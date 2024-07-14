package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private int pointId;
    private int userId;
    private int pointsEarned;
    private String activityType;
    private Timestamp activityDate;
}
