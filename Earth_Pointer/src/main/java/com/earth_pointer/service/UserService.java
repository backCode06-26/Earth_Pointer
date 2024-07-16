package com.earth_pointer.service;

import com.earth_pointer.domain.User;
import com.earth_pointer.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public void join(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null) {

            userRepository.join(user);
        } else {
            throw new RuntimeException("이미 가입된 유저가 있습니다.");
        }
    }

    public void passwordChange(String email, String password) {
        userRepository.changePassword(email, password);
    }

    public int findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.getUserId();
    }
}
