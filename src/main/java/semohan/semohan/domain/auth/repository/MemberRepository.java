package semohan.semohan.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import semohan.semohan.domain.auth.domain.Member;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByPhoneNumber(String phoneNumber);
    Optional<Member> findMemberById(Long id);
    Optional<Member> findMemberByNickname(String nickname);
}
