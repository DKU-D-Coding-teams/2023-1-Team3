package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.database.MemberInterface;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMail(String mail);

    boolean existsByMemberId(Long id);

    Optional<Member> findByMail(String mail);

    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth, m.image as image, m.department as department, m.introduction as introduction from Member as m where m.memberId <> ?1 and m.gender <> ?2 and m.memberId not in (select toId from MemberLog where fromId = ?1)")
    List<MemberInterface> getSuggestQuery(Long memberId, Gender gender);

    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth, m.image as image, m.department as department, m.introduction as introduction from Member as m where m.memberId in (select toId from MemberLog where fromId = ?1 and likeSignal = true) order by m.memberId asc")
    List<MemberInterface> getLikeQuery(Long memberId, Pageable pageable);

    @Transactional
    void deleteMemberByMail(String mail);

}
