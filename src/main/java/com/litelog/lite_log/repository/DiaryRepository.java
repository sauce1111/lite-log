package com.litelog.lite_log.repository;


import com.litelog.lite_log.entity.Diary;
import com.litelog.lite_log.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByMemberAndDate(Member member, LocalDate date);
}

