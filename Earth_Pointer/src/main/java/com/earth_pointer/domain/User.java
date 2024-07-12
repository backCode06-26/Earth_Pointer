package com.earth_pointer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String email;
    private boolean isVerified;
    private Timestamp registrationDate;

}
