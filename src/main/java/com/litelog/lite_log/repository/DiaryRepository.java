package com.litelog.lite_log.repository;


import com.litelog.lite_log.entity.Diary;
import com.litelog.lite_log.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByMemberAndDate(Member member, LocalDate date);

    @Query("SELECT d FROM Diary d JOIN FETCH d.member WHERE d.member = :member AND d.date = :date")
    Optional<Diary> findDiaryFetchMemberByMemberAndDate(@Param("member") Member member, @Param("date") LocalDate date);

    List<Diary> findByMember(Member member);
}

