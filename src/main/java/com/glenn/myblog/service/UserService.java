package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.DuplicateUserEmailException;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto save(UserDto userDto) {
        validateEmailDuplication(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = userDto.toEntity(encodedPassword);
        return UserDto.of(userRepository.save(user));
    }

    private void validateEmailDuplication(UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                    throw new DuplicateUserEmailException();
                });
    }
}