package com.clipstory.clipstoryserver.repository;

import com.clipstory.clipstoryserver.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByCustomId(String customId);

    boolean existsByCustomId(String customId);

}
