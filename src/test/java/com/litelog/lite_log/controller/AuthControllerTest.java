package com.litelog.lite_log.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litelog.lite_log.config.TestSecurityConfig;
import com.litelog.lite_log.dto.LoginRequestDto;
import com.litelog.lite_log.dto.SignupRequestDto;
import com.litelog.lite_log.exception.UserNotFoundException;
import com.litelog.lite_log.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        String url = "/auth/signup";
        String requestUsername = "testUser";
        String requestPassword = "pass12!@";
        String requestEmail = "test@test.com";
        String requestNickname = "testNickname";
        SignupRequestDto requestDto = new SignupRequestDto(requestUsername, requestPassword, requestEmail, requestNickname);

        // when, then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Signup success."));
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 유효성 검사")
    void signup_fail_invalidUsername() throws Exception {
        // given
        String url = "/auth/signup";
        String requestBlankUsername = "   ";
        String requestPassword = "pass12!@";
        String requestEmail = "test@test.com";
        String requestNickname = "testNickname";
        SignupRequestDto requestDto = new SignupRequestDto(requestBlankUsername, requestPassword, requestEmail, requestNickname);

        // when, then
        mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.username").isNotEmpty())
                .andExpect(jsonPath("$.errors.username").value("username is required."));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        String url = "/auth/login";
        String requestUsername = "testUsername";
        String requestPassword = "pass12!@";
        LoginRequestDto requestDto = new LoginRequestDto(requestUsername, requestPassword);
        String mockToken = "mock-jwt-token";

        Mockito.when(authService.login(any(LoginRequestDto.class))).thenReturn(mockToken);

        // when, then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Login success."))
                .andExpect(jsonPath("$.data.token").value(mockToken));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_fail_userNotFound() throws Exception {
        // given
        String url = "/auth/login";
        String requestUsername = "wrongUsername";
        String requestPassword = "pass12!@";
        LoginRequestDto requestDto = new LoginRequestDto(requestUsername, requestPassword);
        Mockito.when(authService.login(any(LoginRequestDto.class)))
                .thenThrow(new UserNotFoundException());

        // when, then
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("User not found."));
    }
}