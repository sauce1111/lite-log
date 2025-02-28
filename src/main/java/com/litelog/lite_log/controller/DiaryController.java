package com.litelog.lite_log.controller;

import com.litelog.lite_log.dto.ApiResponseDto;
import com.litelog.lite_log.dto.DiaryDto;
import com.litelog.lite_log.entity.Diary;
import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.service.DiaryService;
import com.litelog.lite_log.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final MemberService memberservice;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<Void>> createDiary(
                @RequestParam("date") String date, @RequestParam("title") String title,
                @RequestParam("content") String content, @RequestParam(value = "image", required = false) MultipartFile image)
            throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberservice.findByUsername(username);
        diaryService.createDiary(member, LocalDate.parse(date), title, content, image);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "Diary entry created."));
    }

    @GetMapping("/{date}")
    public ResponseEntity<ApiResponseDto<DiaryDto>> getDiary(@PathVariable("date") String date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberservice.findByUsername(username);
        Diary diary = diaryService.getDiary(member, LocalDate.parse(date));
        DiaryDto diaryDto = new DiaryDto(diary);

        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "Diary found.", diaryDto));
    }
}
