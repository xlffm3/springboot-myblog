package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.DuplicatedUserEmailException;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user = User.builder()
            .name("tester")
            .email("tester@naver.com")
            .password(new BCryptPasswordEncoder().encode("abcDE!123"))
            .build();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("패스워드를 생략한 회원 리스트 조회")
    @Test
    public void 회원_리스트_조회_패스워드_제외() {
        List<User> users = Arrays.asList(user);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.findAll();
        UserDto userDto = userDtos.get(0);

        assertThat(userDto.getName()).isEqualTo("tester");
        assertThat(userDto.getPassword()).isNull();
    }

    @DisplayName("회원 생성")
    @Test
    public void 회원_생성() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto userDto = UserDto.builder()
                .name("tester")
                .email("tester@naver.com")
                .password("abcDE!123")
                .build();
        UserDto resultDto = userService.save(userDto);

        assertThat(resultDto.getName()).isEqualTo("tester");
        assertThat(resultDto.getEmail()).isEqualTo("tester@naver.com");

        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(eq("abcDE!123"));
    }

    @DisplayName("회원 생성 실패, 이메일 중복")
    @Test
    public void 회원_생성_실패_이메일_중복() {
        UserDto userDto = UserDto.of(user);

        when(userRepository.findByEmail("tester@naver.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> {
            userService.save(userDto);
        }).isInstanceOf(DuplicatedUserEmailException.class);

        verify(userRepository, times(1)).findByEmail(eq("tester@naver.com"));
        verify(userRepository, never()).save(any());
    }

    @DisplayName("회원 삭제 성공")
    @Test
    public void 회원_삭제_성공() {
        String password = "abcDE!123";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        userService.deleteUser(1L, password);

        verify(userRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @DisplayName("회원 삭제 실패, 비밀번호 불일치")
    @Test
    public void 회원_삭제_실패_비밀번호_불일치() {
        String password = "wrongpass";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> {
            userService.deleteUser(1L, password);
        }).isInstanceOf(WrongPasswordException.class);

        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @DisplayName("회원 삭제 실패, 유저가 없음")
    @Test
    public void 회원_삭제_실패_유저_없음() {
        String password = "wrongpass";

        when(userRepository.findById(2L)).thenThrow(WrongEmailException.class);

        assertThatThrownBy(() -> {
            userService.deleteUser(2L, password);
        }).isInstanceOf(WrongEmailException.class);
    }
}
