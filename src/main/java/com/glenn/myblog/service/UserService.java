package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.DuplicatedUserEmailException;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        validateEmailDuplication(userDto);
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = userDto.toEntity(encodedPassword);
        return UserDto.of(userRepository.save(user));
    }

    private void validateEmailDuplication(UserDto userDto) {
        String email = userDto.getEmail();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new DuplicatedUserEmailException(email);
                });
    }

    @Transactional
    public void deleteUser(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(WrongEmailException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException();
        }
        userRepository.deleteById(id);
    }
}
