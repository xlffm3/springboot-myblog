package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void 정상_로그인() {
        String loginEmail = "k123@gmail.com";
        String loginPassword = "Pass!123";
        String encryptedPassword = new BCryptPasswordEncoder().encode(loginPassword);

        User user = User.builder()
                .name("jipark")
                .email(loginEmail)
                .password(encryptedPassword)
                .build();
        LoginDto loginDto = LoginDto.builder()
                .email(loginEmail)
                .password(loginPassword)
                .build();

        when(userRepository.findByEmail(loginEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginPassword, encryptedPassword)).thenReturn(true);
        LoginDto responseDto = loginService.login(loginDto);

        assertThat(responseDto.getName()).isEqualTo("jipark");
    }

    @Test
    public void 로그인_실패_패스워드가_다름() {
        String loginEmail = "k123@gmail.com";
        String loginPassword = "Pass!123";
        String encryptedPassword = new BCryptPasswordEncoder().encode(loginPassword);

        User user = User.builder()
                .name("jipark")
                .email(loginEmail)
                .password(encryptedPassword)
                .build();
        LoginDto loginDto = LoginDto.builder()
                .email(loginEmail)
                .password("WrongPass!123")
                .build();

        when(userRepository.findByEmail(loginEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginPassword, encryptedPassword)).thenReturn(false);

        assertThatThrownBy(() -> {
            loginService.login(loginDto);
        }).isInstanceOf(WrongPasswordException.class);
    }

    @Test
    public void 로그인_실패_유저_없음() {
        String loginEmail = "k123@gmail.com";
        String loginPassword = "Pass!123";

        LoginDto loginDto = LoginDto.builder()
                .email(loginEmail)
                .password(loginPassword)
                .build();
        when(userRepository.findByEmail(loginEmail)).thenThrow(WrongEmailException.class);

        assertThatThrownBy(() -> {
            loginService.login(loginDto);
        }).isInstanceOf(WrongEmailException.class);
    }
}
