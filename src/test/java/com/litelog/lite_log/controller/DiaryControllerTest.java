package com.litelog.lite_log.controller;

import com.litelog.lite_log.config.TestSecurityConfig;
import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.exception.DiaryNotFoundException;
import com.litelog.lite_log.service.DiaryService;
import com.litelog.lite_log.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DiaryController.class)
@Import(TestSecurityConfig.class)
class DiaryControllerTest extends BaseControllerTest {

    @MockBean
    private DiaryService diaryService;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("Diary 작성 성공")
    void createDiary_success() throws Exception {
        // given
        String username = "testUser";
        String date = "2025-02-26";
        String title = "diary!!";
        String content = "test content!!";

        Member member = new Member(username, "pass12!@", "test@test.com", null);
        MockMultipartFile mockImage = new MockMultipartFile("imageName", "test.jpg",
                "image/jpeg", "mock-image-content".getBytes());
        Mockito.when(memberService.findByUsername(username)).thenReturn(member);
        Mockito.doNothing().when(diaryService).createDiary(eq(member), any(LocalDate.class), eq(title), eq(content), any());

        // when, then
        mockMvc.perform(multipart("/diary/create")
                        .file(mockImage)
                        .param("username", username)
                        .param("date", date)
                        .param("title", title)
                        .param("content", content))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Diary entry created."));
    }

    @Test
    @DisplayName("diary 조회 실패 - diary 없음")
    void getDiary_fail_DiaryNotFound() throws Exception {
        // given
        String username = "testUser";
        LocalDate date = LocalDate.parse("2025-02-26");
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        Mockito.when(memberService.findByUsername(username)).thenReturn(
                new Member(username, "pass12!@", "test@test.com", null));
        Mockito.when(diaryService.getDiary(any(), eq(date))).thenThrow(
                new DiaryNotFoundException("Diary not found."));

        // when, then
        performGetRequest("/diary/2025-02-26", params, HttpStatus.NOT_FOUND, "Diary not found.");
    }
}
