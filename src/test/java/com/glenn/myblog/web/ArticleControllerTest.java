package com.glenn.myblog.web;

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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("게시글 작성")
    @BeforeEach
    public void createPost() {
        webTestClient.method(HttpMethod.POST)
                .uri("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters
                        .fromFormData("id", "1")
                        .with("authorName", "tester")
                        .with("content", "test"))
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("게시글 조회")
    @Test
    public void readPost() {
        webTestClient.method(HttpMethod.GET)
                .uri("/articles/1")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
