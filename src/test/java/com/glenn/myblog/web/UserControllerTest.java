package com.glenn.myblog.web;

import com.glenn.myblog.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static long USER_ID;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트 회원 가입")
    @BeforeEach
    public void createUser() {
        webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", "tester")
                        .with("password", "aBcdEf!123")
                        .with("email", "tester@gmail.com"))
                .exchange()
                .expectStatus()
                .is3xxRedirection()
                .expectHeader()
                .valueMatches(HttpHeaders.LOCATION, ".*/login.*");
        USER_ID = userRepository.findByEmail("tester@gmail.com")
                .orElseThrow(RuntimeException::new)
                .getId();
    }

    @DisplayName("테스트 회원 계정 삭제")
    @AfterEach
    public void deleteUser() {
        userRepository.deleteById(USER_ID);
    }

    @DisplayName("Email 양식 오류")
    @Test
    public void createUserWhenEmailInvalid() {
        expectStatus("json", "aBcdEf!123", "wrongemailck")
                .isBadRequest();
    }

    @DisplayName("Email 중복 예외")
    @Test
    public void createUserWhenEmailDuplicated() {
        expectStatus("json", "aBcdEf!123", "tester@gmail.com")
                .isNotFound();
    }

    @DisplayName("이름 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"jkjkjkjkppj", "jkj!k!3"})
    public void createUserWhenNameInvalid(String name) {
        expectStatus(name, "aBcdEf!123", "tester23@gmail.com")
                .isBadRequest();
    }

    @DisplayName("비밀번호 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"aBcde!2", "ABCD!123", "abcd!123", "abcde123", "abcdefghi"})
    public void createUserWhenPasswordInvalid(String password) {
        expectStatus("tester", password, "tester66@gmail.com")
                .isBadRequest();
    }

    private StatusAssertions expectStatus(String name, String password, String email) {
        return webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("password", password)
                        .with("email", email))
                .exchange()
                .expectStatus();
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
