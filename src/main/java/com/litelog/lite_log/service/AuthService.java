package com.litelog.lite_log.service;

import com.litelog.lite_log.config.JwtTokenProvider;
import com.litelog.lite_log.dto.LoginRequestDto;
import com.litelog.lite_log.dto.SignupRequestDto;
import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.exception.DuplicateUsernameException;
import com.litelog.lite_log.exception.InvalidPasswordException;
import com.litelog.lite_log.exception.UserNotFoundException;
import com.litelog.lite_log.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String newUsername = requestDto.getUsername();
        boolean existsByUsername = memberRepository.existsByUsername(newUsername);
        if (existsByUsername) {
            throw new DuplicateUsernameException(newUsername);
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member newMember =
                new Member(newUsername, encodedPassword, requestDto.getEmail(), requestDto.getNickname());
        memberRepository.save(newMember);
    }

    public String login(LoginRequestDto requestDto) {
        Member findMember = memberRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())) {
            throw new InvalidPasswordException();
        }

        UserDetails userDetails = new User(findMember.getUsername(), findMember.getPassword(), Collections.emptyList());

        return jwtTokenProvider.createToken(userDetails);
    }
}
