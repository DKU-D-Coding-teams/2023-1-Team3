package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMail(String mail);

    Optional<Member> findMemberByMail(String mail);

    @Query(value = "select m.refreshToken from Member as m where m.mail = ?1")
    String findRefreshTokenByMail(String mail);

//    @Query("UPDATE Question q set q.showCount = q.showCount + 1 where q.questionId = :questionId")
    @Modifying
    @Query(value = "update Member as m set m.refreshToken = ?2 where m.mail = ?1")
    void saveRefreshTokenByMail(String mail, String refreshToken);

    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth from Member as m where m.memberId <> ?1 and m.gender <> ?2 and m.memberId not in (select toId from MemberLog where fromId = ?1)")
    List<MemberListInterface> getSuggestQuery(Long memberId, Gender gender);

    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth from Member as m where m.memberId in (select toId from MemberLog where fromId = ?1 and signal = true) order by m.memberId asc")
    List<MemberListInterface> getLikeQuery(Long memberId, Pageable pageable);

}
