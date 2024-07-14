package com.earth_pointer.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsedFurniture {
    private int userFurnitureId;
    private int userId;
    private int furnitureId;
    private int roomPositionX;
    private int roomPositionY;
}
