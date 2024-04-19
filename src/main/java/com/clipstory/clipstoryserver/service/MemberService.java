package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findOrCreateMember(String customId, String name) {
        Member member = null;
        try{
           member = findMemberByCustomId(customId);
        }catch (GeneralException e) {
            createMember(customId,name);
            member = findMemberByCustomId(customId);
        }
        return member;
    }

    public void createMember(String customId, String name) {
        Member member = Member.toEntity(customId, name);
        memberRepository.save(member);
    }

    public Member findMemberByCustomId(String customId) {
        return memberRepository.findMemberByCustomId(customId)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

}