package com.glenn.myblog.web.controller;

import com.glenn.myblog.domain.entity.User;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {
    private static final String TESTER_NAME = "tester";
    private static final String TESTER_PASSWORD = "Password!12";
    private static final String TESTER_EMAIL = "springtester@gmail.com";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void 테스트_유저_생성() {
        webTestClient.method(HttpMethod.POST)
                .uri("/users")
                .body(BodyInserters.fromFormData("name", TESTER_NAME)
                        .with("email", TESTER_EMAIL)
                        .with("password", TESTER_PASSWORD))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @AfterEach
    public void 테스트_유저_삭제() {
        User user = userRepository.findByEmail(TESTER_EMAIL).get();
        userRepository.delete(user);
    }

    @DisplayName("정상적으로 로그인 처리")
    @Test
    public void 로그인_성공() {
        expectLoginStatus(TESTER_EMAIL, TESTER_PASSWORD).is3xxRedirection();
    }

    @DisplayName("로그인 실패, 가입하지 않은 이메일")
    @Test
    public void 로그인_실패_가입하지_않은_이메일() {
        StatusAssertions statusAssertions = expectLoginStatus("wrongspringtester@gmail.com", TESTER_PASSWORD);
        testErrorMessage(WrongEmailException.ERROR_MESSAGE, statusAssertions);
    }

    @DisplayName("로그인 실패, 비밀번호 오류")
    @Test
    public void 로그인_실패_비밀번호_오류() {
        StatusAssertions statusAssertions = expectLoginStatus(TESTER_EMAIL, "wrongpass");
        testErrorMessage(WrongPasswordException.ERROR_MESSAGE, statusAssertions);
    }

    private StatusAssertions expectLoginStatus(String email, String password) {
        return webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", email)
                        .with("password", password))
                .exchange()
                .expectStatus();
    }

    private void testErrorMessage(String errorMessage, StatusAssertions statusAssertions) {
        statusAssertions.isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(errorMessage);
                });
    }
}
