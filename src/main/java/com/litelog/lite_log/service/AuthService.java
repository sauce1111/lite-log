package com.litelog.lite_log.service;

import com.litelog.lite_log.config.JwtTokenProvider;
import com.litelog.lite_log.dto.LoginRequestDto;
import com.litelog.lite_log.dto.SignupRequestDto;
import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.exception.InvalidPasswordException;
import com.litelog.lite_log.exception.UserNotFoundException;
import com.litelog.lite_log.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member newMember =
                new Member(requestDto.getUsername(), encodedPassword, requestDto.getEmail(), requestDto.getNickname());
        memberRepository.save(newMember);
    }

    public String login(LoginRequestDto requestDto) {
        Member findMember = memberRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())) {
            throw new InvalidPasswordException();
        }

        return jwtTokenProvider.createToken(findMember.getUsername());
    }
}
