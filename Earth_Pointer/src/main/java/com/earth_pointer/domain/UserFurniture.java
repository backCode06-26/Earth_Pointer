package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFurniture {
    private String username;
    private String name;
    private String description;
    private int price;
}
