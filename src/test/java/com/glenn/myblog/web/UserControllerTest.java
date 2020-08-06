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

    @DisplayName("비밀번호는 8자 이상의 소문자, 대문자, 숫자, 특수 문자의 조합이다")
    @Test
    public void createUserWhenPasswordInvalid() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "aBcde!2") //조건 충족 but 8자가 아닐때
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "ABCD!123") //소문자가 없을때
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "abcd!123") //대문자가 없을때
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "abcde123") //특수문자가 없을때
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "abcdefghi") //숫자가 없을때
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .isBadRequest();

        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "tester")
                        .with("password", "abcCe!123") //정상
                        .with("email", "tester23@gmail.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection();
    }


    @DisplayName("회원 목록 페이지로 이동")
    @Test
    public void moveToUserListPage() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                .isOk();
    }
}
