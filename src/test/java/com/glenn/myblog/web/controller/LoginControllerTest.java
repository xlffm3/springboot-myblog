package com.glenn.myblog.web.controller;

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

    @DisplayName("정상적으로 로그인된다.")
    @Test
    public void loginOk() {
        webTestClient.method(HttpMethod.POST)
                .uri("/login")
                .body(BodyInserters.fromFormData("email", "jipark@gmail.com")
                        .with("password", "Testpass!123"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}
