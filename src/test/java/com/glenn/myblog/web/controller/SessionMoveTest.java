package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.LoginDto;
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

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionMoveTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToWebHandler(exchange -> {
            return exchange.getSession()
                    .doOnNext(webSession -> {
                        webSession.getAttributes().put("loginDto", LoginDto.builder()
                                .id(1L).email("tester@gmail.com").password("Pass!123").build());
                    })
                    .then();
        }).build();
    }

    @DisplayName("Login Session이 없으면 글 작성 페이지로 이동이 불가능")
    @Test
    public void 글_작성_페이지_이동_불가능() {
        webTestClient.method(HttpMethod.GET)
                .uri("/signup")
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }
}
