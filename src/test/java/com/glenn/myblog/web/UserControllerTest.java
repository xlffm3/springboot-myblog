package com.glenn.myblog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @DisplayName("회원 가입")
    @BeforeEach
    public void createUser() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "json")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester@gmail.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }

    @DisplayName("Email 양식 오류")
    @Test
    public void createUserWhenEmailInvalid() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "json")
                        .with("password", "aBcdEf!123")
                        .with("email", "wrongemailck"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @DisplayName("Email 중복 예외")
    @Test
    public void createUserWhenEmailDuplicated() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "json")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @DisplayName("이름 2~10글자 범위 외 혹은 특수문자나 기호 포함시 회원 생성 불가")
    @Test
    public void createUserWhenNameInvalid() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "jkjkjkjkppj")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "jkj!k!3")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
