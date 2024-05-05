package com.clipstory.clipstoryserver.global.auth;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String customId) throws UsernameNotFoundException {
        Member member =  memberRepository.findMemberByCustomId(customId)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
        return new Member(member.getCustomId(), member.getPassword(), member.getRole());
    }

}

