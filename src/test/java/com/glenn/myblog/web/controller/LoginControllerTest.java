package com.glenn.myblog.web.controller;

import com.glenn.myblog.domain.entity.User;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void createUser() {
        webTestClient.method(HttpMethod.POST)
                .uri("/users")
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("email", "springtester@gmail.com")
                        .with("password", "Password!12"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @AfterEach
    public void tearDown() {
        User user = userRepository.findByEmail("springtester@gmail.com").get();
        userRepository.delete(user);
    }

    @DisplayName("정상적으로 로그인된다.")
    @Test
    public void loginOk() {
        webTestClient.method(HttpMethod.POST)
                .uri("/login")
                .body(BodyInserters.fromFormData("email", "springtester@gmail.com")
                        .with("password", "Password!12"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @DisplayName("정상적으로 로그아웃된다.")
    @Test
    public void logoutOk() {
        webTestClient.method(HttpMethod.GET)
                .uri("/logout")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}
