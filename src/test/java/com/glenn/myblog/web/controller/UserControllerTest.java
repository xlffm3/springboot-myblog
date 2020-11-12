package com.glenn.myblog.web.controller;

import com.glenn.myblog.domain.exception.DuplicatedUserEmailException;
import com.glenn.myblog.domain.exception.WrongEmailException;
import com.glenn.myblog.domain.exception.WrongPasswordException;
import com.glenn.myblog.domain.repository.UserRepository;
import com.glenn.myblog.dto.LoginDto;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.glenn.myblog.dto.UserDto.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static final String TESTER_NAME = "tester";
    private static final String TESTER_PASSWORD = "aBcdEf!123";
    private static final String TESTER_EMAIL = "tester@gmail.com";
    private static long USER_ID;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("테스트 회원 가입")
    @BeforeEach
    public void 테스트_유저_생성() {
        expectSignUpStatus(TESTER_NAME, TESTER_PASSWORD, TESTER_EMAIL)
                .is3xxRedirection()
                .expectHeader()
                .valueMatches(HttpHeaders.LOCATION, ".*/login.*");
        USER_ID = userRepository.findByEmail(TESTER_EMAIL)
                .orElseThrow(RuntimeException::new)
                .getId();
    }

    @DisplayName("테스트 회원 계정 삭제")
    @AfterEach
    public void 테스트_유저_삭제() {
        userRepository.deleteById(USER_ID);
    }

    @DisplayName("회원가입 시, Email 중복 예외")
    @Test
    public void 회원가입_실패_이메일_중복() {
        StatusAssertions statusAssertions = expectSignUpStatus(TESTER_NAME, TESTER_PASSWORD, TESTER_EMAIL);
        testErrorMessage(String.format(DuplicatedUserEmailException.ERROR_MESSAGE, TESTER_EMAIL), statusAssertions);
    }

    @DisplayName("회원가입 시, Email 양식 오류")
    @Test
    public void 회원가입_실패_이메일_양식_오류() {
        StatusAssertions statusAssertions = expectSignUpStatus(TESTER_NAME, TESTER_PASSWORD, "wrongemail");
        testErrorMessage(EMAIL_FORMAT_ERROR, statusAssertions);
    }

    @DisplayName("회원가입 시, Email 공백 오류")
    @Test
    public void 회원가입_실패_이메일_공백_오류() {
        StatusAssertions statusAssertions = expectSignUpStatus(TESTER_NAME, TESTER_PASSWORD, "");
        testErrorMessage(EMAIL_BLANK_ERROR, statusAssertions);
    }

    @DisplayName("회원가입 시, 이름이 2글자 미만 오류")
    @Test
    public void 회원가입_실패_이름_2글자_미만_오류() {
        StatusAssertions statusAssertions = expectSignUpStatus("a", TESTER_PASSWORD, "tester23@gmail.com");
        testErrorMessage(NAME_LENGTH_ERROR, statusAssertions);
    }

    @DisplayName("회원가입 시, 이름 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"jkjkjkjkppj", "jkj!k!3"})
    public void 회원가입_실패_이름_양식_오류(String name) {
        StatusAssertions statusAssertions = expectSignUpStatus(name, TESTER_PASSWORD, "tester23@gmail.com");
        testErrorMessage(NAME_FORMAT_ERROR, statusAssertions);
    }

    @DisplayName("회원가입 시, 비밀번호 양식 오류")
    @ParameterizedTest
    @ValueSource(strings = {"aBcde!2", "ABCD!123", "abcd!123", "abcde123", "abcdefghi"})
    public void 회원가입_실패_비밀번호_양식_오류(String password) {
        StatusAssertions statusAssertions = expectSignUpStatus(TESTER_NAME, password, "tester23@gmail.com");
        testErrorMessage(PASSWORD_FORMAT_ERROR, statusAssertions);
    }

    @DisplayName("회원 목록 페이지로 이동")
    @Test
    public void 회원_목록_페이지로_이동() {
        webTestClient.get()
                .uri("/users/list")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(TESTER_NAME);
                    assertThat(body).contains(TESTER_EMAIL);
                });
    }

    @DisplayName("회원탈퇴 실패, 입력 비밀번호 다름")
    @Test
    public void 회원탈퇴_실패_입력_비밀번호_다름() {
        StatusAssertions statusAssertions = expectWithdrawStatus(getSessionWebTestClient(USER_ID), "wrongpass");
        testErrorMessage(WrongPasswordException.ERROR_MESSAGE, statusAssertions);
    }

    @DisplayName("회원탈퇴 실패, 회원 세션 아이디 조회 불가")
    @Test
    public void 회원탈퇴_실패_회원_세션_아이디_조회_불가() {
        StatusAssertions statusAssertions = expectWithdrawStatus(getSessionWebTestClient(888L), "wrongpass");
        testErrorMessage(WrongEmailException.ERROR_MESSAGE, statusAssertions);
    }

    @DisplayName("회원탈퇴 성공")
    @Test
    public void 회원탈퇴_성공() {
        expectSignUpStatus("mockuser", "paddkK!13", "guqamole13@naver.com");
        long userId = userRepository.findByEmail("guqamole13@naver.com")
                .orElseThrow(RuntimeException::new)
                .getId();
        expectWithdrawStatus(getSessionWebTestClient(userId), "paddkK!13")
                .is3xxRedirection();
    }

    private StatusAssertions expectSignUpStatus(String name, String password, String email) {
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

    private StatusAssertions expectWithdrawStatus(WebTestClient webTestClient, String password) {
        return webTestClient.method(HttpMethod.POST)
                .uri("/users/withdraw")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("password", password))
                .exchange()
                .expectStatus();
    }

    private void testErrorMessage(String errorMessage, StatusAssertions statusAssertions) {
        statusAssertions.isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains(errorMessage);
                    System.out.println(body);
                });
    }

    private WebTestClient getSessionWebTestClient(Long id) {
        return WebTestClient.bindToWebHandler(exchange -> {
            return exchange.getSession()
                    .doOnNext(webSession -> {
                        webSession.getAttributes()
                                .put("loginDto", LoginDto.builder()
                                        .id(id)
                                        .build());
                    }).then();
        }).build();
    }
}
