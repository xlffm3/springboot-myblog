package com.glenn.myblog.web.controller;

import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.glenn.myblog.dto.UserDto.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static long USER_ID;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트 회원 가입")
    @BeforeEach
    public void createUser() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "tester")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester@gmail.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches(HttpHeaders.LOCATION, ".*/login.*");
        USER_ID = userRepository.findByEmail("tester@gmail.com")
                .orElseThrow(RuntimeException::new)
                .getId();
    }

    @DisplayName("테스트 회원 계정 삭제")
    @AfterEach
    public void deleteUser() {
        userRepository.deleteById(USER_ID);
    }

    @DisplayName("Email 중복 예외")
    @Test
    public void createUserWhenEmailDuplicated() {
        expectStatus("There is already same email. (Email : tester@gmail.com)", "json", "aBcdEf!123", "tester@gmail.com");
    }

    @DisplayName("Email 양식 오류")
    @Test
    public void createUserWhenEmailInvalid() {
        expectStatus(EMAIL_FORMAT_ERROR, "json", "aBcdEf!123", "wrongemailck");
    }

    @DisplayName("Email이 비어있는 경우")
    @Test
    public void createUserWhenEmailBlank() {
        expectStatus(EMAIL_BLANK_ERROR, "json", "aBcdEf!123", "");
    }

    @DisplayName("이름이 2글자 미만인 경우")
    @Test
    public void createUserWhenNameIsShort() {
        expectStatus(NAME_LENGTH_ERROR, "a", "aBcdEf!123", "tester23@gmail.com");
    }

    @DisplayName("이름 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"jkjkjkjkppj", "jkj!k!3"})
    public void createUserWhenNameInvalid(String name) {
        expectStatus(NAME_FORMAT_ERROR, name, "aBcdEf!123", "tester23@gmail.com");
    }

    @DisplayName("비밀번호 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"aBcde!2", "ABCD!123", "abcd!123", "abcde123", "abcdefghi"})
    public void createUserWhenPasswordInvalid(String password) {
        expectStatus(PASSWORD_FORMAT_ERROR, "tester", password, "tester66@gmail.com");
    }

    private void expectStatus(String errorMessage, String name, String password, String email) {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(errorMessage);
                });
    }

    @DisplayName("회원 목록 페이지로 이동")
    @Test
    public void moveToUserListPage() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains("tester");
                    assertThat(body).contains("tester@gmail.com");
                });
    }

    @DisplayName("입력 비밀번호가 다를 경우 회원 삭제 불가")
    @Test
    public void cannotDeleteUser() {
        webTestClient.method(HttpMethod.POST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("password", "WrongPass!123"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(WrongPasswordException.ERROR_MESSAGE);
                });
    }
}
