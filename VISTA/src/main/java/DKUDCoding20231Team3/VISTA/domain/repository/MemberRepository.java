package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMail(String mail);

    Optional<Member> findByMail(String mail);

    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth from Member as m where m.memberId in (select toId from MemberLog where fromId = ?1 and signal = true) order by m.memberId asc")
    List<MemberListInterface> getLikeQuery(Long memberId, Pageable pageable);

}
