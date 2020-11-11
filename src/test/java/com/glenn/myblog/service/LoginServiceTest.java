package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.LoginDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void 정상_로그인() {
        String loginEmail = "k123@gmail.com";
        String loginPassword = "Pass!123";

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .name("jipark")
                .email(loginEmail)
                .password(passwordEncoder.encode(loginPassword))
                .build();
        LoginDto loginDto = LoginDto.builder()
                .email(loginEmail)
                .password(loginPassword)
                .build();

        when(userRepository.findByEmail(loginEmail)).thenReturn(Optional.of(user));
        LoginDto responseDto = loginService.login(loginDto);

        assertThat(responseDto.getName()).isEqualTo("jipark");
    }
}
