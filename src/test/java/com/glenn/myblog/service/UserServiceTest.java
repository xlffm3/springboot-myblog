package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.DuplicatedUserEmailException;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("회원 목록 조회")
    @Test
    public void findAll() {
        List<User> users =
                Arrays.asList(User.builder().name("tester").email("tester@naver.com").password("encoded").build());

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.findAll();
        UserDto userDto = userDtos.get(0);
        assertThat(userDto.getName()).isEqualTo("tester");
        assertThat(userDto.getPassword()).isNull();
    }

    @DisplayName("회원 정상 생성")
    @Test
    public void save() {
        UserDto userDto = UserDto.builder()
                .name("json")
                .email("tester@gmail.com")
                .password("dkansk")
                .build();

        User user = User.builder()
                .name("json")
                .email("tester@gmail.com")
                .build();

        when(userRepository.save(any()))
                .thenReturn(user);
        UserDto resultDto = userService.save(userDto);

        assertThat(resultDto.getName()).isEqualTo("json");
        assertThat(resultDto.getEmail()).isEqualTo("tester@gmail.com");
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(eq("dkansk"));
    }

    @DisplayName("이메일 중복 시 회원 생성 불가")
    @Test
    public void saveWhenDuplicateEmail() {
        UserDto userDto = UserDto.builder()
                .name("json")
                .email("tester@gmail.com")
                .password("dkansk")
                .build();
        User user = User.builder()
                .name("duplicated")
                .email("tester@gmail.com")
                .password("password")
                .build();

        when(userRepository.findByEmail("tester@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> {
            userService.save(userDto);
        }).isInstanceOf(DuplicatedUserEmailException.class);

        verify(userRepository, times(1)).findByEmail(eq("tester@gmail.com"));
        verify(userRepository, never()).save(any());
    }
}
