package com.litelog.lite_log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column
    private String menuImage;

    public Diary(Member member, LocalDate date, String title, String content, String menuImage) {
        this.member = member;
        this.date = date;
        this.title = title;
        this.content = content;
        this.menuImage = menuImage;
    }
}











