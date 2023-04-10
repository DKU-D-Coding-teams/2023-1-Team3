package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMail(String mail);

    Optional<Member> findByMail(String mail);

//    Optional<Member> findById(Long memberId);
    Member findByMemberId(Long memberId);

    Member findByMemberIdNot(Long memberId);

    @Query(value = "select memberId " +
            "from Member " +
            "where memberId <> ?1")
    List<Long> findMemberIdByMemberIdNot(Long memberId);

    @Query(value = "select m.memberId, m.name, m.gender, m.birth from Member as m where m.memberId <> ?1 and m.memberId not in (select toId from MemberLog where fromId = ?1)")
    List temp(Long memberId);

}
