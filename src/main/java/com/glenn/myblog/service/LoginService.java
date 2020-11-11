package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .get();
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException();
        }
        return LoginDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
