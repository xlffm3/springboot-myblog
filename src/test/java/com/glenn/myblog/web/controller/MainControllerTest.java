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

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("메인 페이지로 이동")
    @Test
    public void moveToIndexPage() {
        isStatusOk(HttpMethod.GET, "/");
    }

    @DisplayName("게시글 작성 페이지로 이동")
    @Test
    public void moveToWritingPage() {
        isStatusOk(HttpMethod.GET, "/writing");
    }

    @DisplayName("회원가입 페이지로 이동")
    @Test
    public void moveToSignUpPage() {
        isStatusOk(HttpMethod.GET, "/signup");
    }

    @DisplayName("로그인 페이지로 이동")
    @Test
    public void moveToLoginPage() {
        isStatusOk(HttpMethod.GET, "/login");
    }

    private void isStatusOk(HttpMethod httpMethod, String uri) {
        webTestClient.method(httpMethod)
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
