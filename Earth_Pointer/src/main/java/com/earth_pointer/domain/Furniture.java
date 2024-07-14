package com.earth_pointer.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Furniture {
    private int furnitureId;     // 가구의 고유 식별자
    private String name;         // 가구의 이름
    private String description;  // 가구의 설명
    private int price;           // 가구의 가격
}
