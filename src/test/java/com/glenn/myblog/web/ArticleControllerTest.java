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
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("테스트 게시글 작성")
    @BeforeEach
    public void createArticle() {
        webTestClient.method(HttpMethod.POST)
                .uri("/articles")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "sampletitle")
                        .with("authorName", "tester")
                        .with("content", "testcontent"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @DisplayName("게시글 조회")
    @Test
    public void readArticle() {
        expectStatus(HttpMethod.GET, "/articles/1")
                .isOk();
    }

    @DisplayName("게시글 수정 페이지로 이동")
    @Test
    public void moveToArticleEditPage() {
        expectStatus(HttpMethod.GET, "/articles/1/edit")
                .isOk();
    }

    @DisplayName("게시글 수정")
    @Test
    public void updateArticle() {
        webTestClient.method(HttpMethod.PUT)
                .uri("/articles/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("title", "test")
                        .with("authorName", "tester")
                        .with("content", "updated"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @DisplayName("게시글 삭제")
    @Test
    public void deleteArticle() {
        expectStatus(HttpMethod.DELETE, "/articles/1")
                .is3xxRedirection();
    }

    private StatusAssertions expectStatus(HttpMethod httpMethod, String uri) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .exchange()
                .expectStatus();
    }
}
