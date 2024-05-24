package com.clipstory.clipstoryserver.service;

import com.clipstory.clipstoryserver.domain.Genre;
import com.clipstory.clipstoryserver.domain.Member;
import com.clipstory.clipstoryserver.domain.Movie;
import com.clipstory.clipstoryserver.domain.Rating;
import com.clipstory.clipstoryserver.global.response.GeneralException;
import com.clipstory.clipstoryserver.global.response.Status;
import com.clipstory.clipstoryserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final GenreService genreService;

    private final HashMap<Long, List<Double>> memberPoses = new HashMap<>();

    public Member findOrCreateMember(String customId) {
        Member member = null;
        try{
           member = findMemberByCustomId(customId);
        }catch (GeneralException e) {
            createMember(customId,customId,customId);
            member = findMemberByCustomId(customId);
        }
        return member;
    }

    public void createMember(String customId, String name, String password) {
        Member member = Member.toEntity(customId, name, password, passwordEncoder);
        memberRepository.save(member);
    }

    public Member findMemberByCustomId(String customId) {
        return memberRepository.findMemberByCustomId(customId)
                .orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GeneralException(Status.MEMBER_NOT_FOUND));
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    public void save(Member member){
        memberRepository.save(member);
    }

    public boolean isMemberExist(String customId) {
        return memberRepository.existsByCustomId(customId);
    }

    public List<Double> getMemberPos(List<Movie> likeMovie, List<Movie> hateMovie) {
        List<Double> memberPos = new ArrayList<>(Collections.nCopies((int)genreService.genreSize(), 0.0));

        for (Movie movie : likeMovie) {
            for (Genre genre : movie.getGenres()) {
                int idx = genre.getId().intValue() - 1;
                memberPos.set(idx, memberPos.get(idx) + 2.0);
            }
        }
        for (Movie movie : hateMovie) {
            for (Genre genre : movie.getGenres()) {
                int idx = genre.getId().intValue() - 1;
                memberPos.set(idx, memberPos.get(idx) - 2.0);
            }
        }

        normalizeMemberPos(memberPos);

        return memberPos;
    }

    public List<Double> getMemberPos(Long memberId) {
        if (memberPoses.get(memberId) != null) {
            return memberPoses.get(memberId);
        }

        Member member = findById(memberId);
        List<Double> moviePos = new ArrayList<>(Collections.nCopies((int)genreService.genreSize(), 0.0));

        for (Rating rating : member.getRatingList()) {
            Movie movie = rating.getMovie();
            for (Genre genre : movie.getGenres()) {
                int idx =  genre.getId().intValue() - 1;
                moviePos.set(idx, moviePos.get(idx) + (rating.getScore() - 3.0));
            }
        }

        normalizeMemberPos(moviePos);

        memberPoses.put(memberId, moviePos);
        return moviePos;
    }

    private void normalizeMemberPos(List<Double> memberPos) {
        double maxValue = 0.0;
        for (int i = 0; i < genreService.genreSize(); i++) {
            maxValue = Math.max(maxValue, Math.abs(memberPos.get(i)));
        }
        if (maxValue == 0.0) {
            return;
        }

        for (int i = 0; i < genreService.genreSize(); i++) {
            memberPos.set(i, memberPos.get(i) / maxValue);
        }
    }

}
