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
import static org.mockito.Mockito.*;

class LoginServiceTest {
    private static final String LOGIN_PASSWORD = "Pass!123";
    private static final String LOGIN_EMAIL = "k123@gmail.com";

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user = User.builder()
            .name("jipark")
            .email(LOGIN_EMAIL)
            .password(new BCryptPasswordEncoder().encode(LOGIN_PASSWORD))
            .build();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void 정상_로그인() {
        LoginDto loginDto = LoginDto.builder()
                .email(LOGIN_EMAIL)
                .password(LOGIN_PASSWORD)
                .build();

        when(userRepository.findByEmail(LOGIN_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

        LoginDto responseDto = loginService.login(loginDto);

        assertThat(responseDto.getName()).isEqualTo("jipark");

        verify(userRepository, times(1)).findByEmail(LOGIN_EMAIL);
        verify(passwordEncoder, times(1)).matches(LOGIN_PASSWORD, user.getPassword());
    }

    @Test
    public void 로그인_실패_패스워드가_다름() {
        LoginDto loginDto = LoginDto.builder()
                .email(LOGIN_EMAIL)
                .password("WrongPass!123")
                .build();

        when(userRepository.findByEmail(LOGIN_EMAIL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> {
            loginService.login(loginDto);
        }).isInstanceOf(WrongPasswordException.class);
    }

    @Test
    public void 로그인_실패_유저_없음() {
        LoginDto loginDto = LoginDto.builder()
                .email("weirdemail@eamil.com")
                .password(LOGIN_PASSWORD)
                .build();

        when(userRepository.findByEmail("weirdemail@eamil.com")).thenThrow(WrongEmailException.class);

        assertThatThrownBy(() -> {
            loginService.login(loginDto);
        }).isInstanceOf(WrongEmailException.class);
    }
}
