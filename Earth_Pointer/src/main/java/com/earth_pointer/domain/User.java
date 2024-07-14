package com.earth_pointer.domain;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String email;
    private boolean isVerified;
    private Timestamp registrationDate;
}
