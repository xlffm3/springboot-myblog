package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.LoginDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainControllerTest {

    @Autowired
    private WebTestClient normalWebTestClient;
    private WebTestClient sessionWebTestClient = WebTestClient.bindToWebHandler(exchange -> {
        return exchange.getSession()
                .doOnNext(webSession -> {
                    webSession.getAttributes()
                            .put("loginDto", LoginDto.builder()
                                    .id(1L).name("tester").email("tester@spring.com").password("Tester!12")
                                    .build());
                }).then();
    }).build();

    @DisplayName("메인 페이지로 이동")
    @Test
    public void 메인_페이지로_이동() {
        expectStatus(normalWebTestClient, HttpMethod.GET, "/").isOk();
    }

    @DisplayName("Logout 상태면, 게시글 작성 페이지 이동 요청시 로그인 페이지로 이동")
    @Test
    public void 로그아웃_상태에서_게시글_작성_페이지가_아닌_로그인_페이지로_이동() {
        expectStatus(normalWebTestClient, HttpMethod.GET, "/writing").is3xxRedirection();
    }

    @DisplayName("Login 상태면, 게시글 작성 페이지 이동 요청시 정상 처리")
    @Test
    public void 로그인_상태에서_게시글_작성_페이지로_이동() {
        expectStatus(sessionWebTestClient, HttpMethod.GET, "/writing").isOk();
    }

    @DisplayName("Logout 상태면, 회원 가입 페이지 이동 요청시 정상 처리")
    @Test
    public void 로그아웃_상태에서_회원가입_페이지로_이동() {
        expectStatus(normalWebTestClient, HttpMethod.GET, "/signup").isOk();
    }

    @DisplayName("Login 상태면, 회원 가입 페이지 이동 요청시 메인 페이지로 이동")
    @Test
    public void 로그인_상태에서_회원가입_페이지가_아닌_메인_페이지로_이동() {
        expectStatus(sessionWebTestClient, HttpMethod.GET, "/signup").is3xxRedirection();
    }

    @DisplayName("Logout 상태면, 로그인 페이지로 이동 요청시 정상 처리")
    @Test
    public void 로그아웃_상태에서_로그인_페이지로_이동() {
        expectStatus(normalWebTestClient, HttpMethod.GET, "/login").isOk();
    }

    @DisplayName("Login 상태면, 로그인 페이지로 이동 요청시 메인 페이지로 이동")
    @Test
    public void 로그인_상태에서_로그인_페이지가_아닌_메인_페이지로_이동() {
        expectStatus(sessionWebTestClient, HttpMethod.GET, "/login").is3xxRedirection();
    }

    @DisplayName("Logout 상태에서, 회원탈퇴 페이지로 이동 요청시 로그인 페이지로 이동")
    @Test
    public void 로그아웃_상태에서_회원탈퇴_페이지가_아닌_로그인_페이지로_이동() {
        expectStatus(normalWebTestClient, HttpMethod.GET, "/withdraw").is3xxRedirection();
    }


    @DisplayName("Login 상태에서, 회원탈퇴 페이지로 이동 요청시 정상 처리")
    @Test
    public void 로그인_상태에서_회원탈퇴_페이지로_이동() {
        expectStatus(sessionWebTestClient, HttpMethod.GET, "/withdraw").isOk();
    }

    private StatusAssertions expectStatus(WebTestClient webTestClient, HttpMethod httpMethod, String uri) {
        return webTestClient.method(httpMethod)
                .uri(uri)
                .exchange()
                .expectStatus();
    }
}
