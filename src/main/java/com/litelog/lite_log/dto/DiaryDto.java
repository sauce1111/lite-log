package com.litelog.lite_log.dto;

import com.litelog.lite_log.entity.Diary;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryDto {

    private String title;
    private String content;
    private String menuImage;
    private String date;

    public DiaryDto(Diary diary) {
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.menuImage = diary.getMenuImage();
        this.date = diary.getDate().toString();
    }
}