package com.glenn.myblog.web.controller;

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

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {
    private static int ARTICLE_ID;

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("테스트 게시글 작성")
    @BeforeEach
    public void 테스트_게시글_작성() {
        webTestClient.method(HttpMethod.POST)
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "sampletitle")
                        .with("authorName", "tester")
                        .with("content", "testcontent"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectBody()
                .consumeWith(response -> {
                    String path = response.getResponseHeaders().getLocation().getPath();
                    int index = path.lastIndexOf("/");
                    ARTICLE_ID = Integer.parseInt(path.substring(index + 1));
                });
    }

    @DisplayName("게시글 조회")
    @Test
    public void 게시글_조회() {
        expectStatus(HttpMethod.GET, "/articles/" + ARTICLE_ID)
                .isOk();
    }

    @DisplayName("게시글 삭제")
    @AfterEach
    public void 게시글_삭제() {
        expectStatus(HttpMethod.DELETE, "/articles/" + ARTICLE_ID)
                .is3xxRedirection();
    }

    @DisplayName("게시글 수정 페이지로 이동")
    @Test
    public void 게시글_수정_페이지로_이동() {
        expectStatus(HttpMethod.GET, "/articles/" + ARTICLE_ID + "/edit")
                .isOk();
    }

    @DisplayName("게시글 수정")
    @Test
    public void 게시글_수정() {
        webTestClient.method(HttpMethod.PUT)
                .uri("/articles/" + ARTICLE_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "test")
                        .with("authorName", "tester")
                        .with("content", "updated"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    private StatusAssertions expectStatus(HttpMethod httpMethod, String uri) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .exchange()
                .expectStatus();
    }
}
