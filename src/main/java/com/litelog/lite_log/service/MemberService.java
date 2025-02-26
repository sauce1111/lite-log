package com.litelog.lite_log.service;

import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.exception.UserNotFoundException;
import com.litelog.lite_log.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }
}
