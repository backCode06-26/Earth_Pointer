package com.earth_pointer.service;

import com.earth_pointer.domain.User;
import com.earth_pointer.dto.CustomUserDetails;
import com.earth_pointer.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryImpl userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email);
        if (userData != null) {
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
