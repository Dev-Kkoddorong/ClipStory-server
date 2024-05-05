package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Role;
import com.clipstory.clipstoryserver.domain.Token;
import com.clipstory.clipstoryserver.global.auth.JwtProvider;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.requestDto.LoginRequestDto;
import com.clipstory.clipstoryserver.responseDto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public JwtResponseDto login(LoginRequestDto loginRequestDto) {
        String customId = loginRequestDto.getCustomId();
        String password = loginRequestDto.getPassword();

        Member member = memberService.findMemberByCustomId(customId);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new GeneralException(Status.MEMBER_UNAUTHORIZED);
        }

        Token token = jwtProvider.generateToken(customId, Role.USER);

        return JwtResponseDto.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

}
