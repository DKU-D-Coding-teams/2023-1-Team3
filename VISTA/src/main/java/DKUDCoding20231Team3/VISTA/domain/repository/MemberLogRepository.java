package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {

    @Transactional
    void deleteByFromId(Long fromId);

//    @Query(value = "select m.memberId as memberId, m.name as name, m.gender as gender, m.birth as birth from Member as m where m.memberId <> ?1 and m.gender <> ?2 and m.memberId not in (select toId from MemberLog where fromId = ?1)")
    void deleteByFromIdAndSignalIsTrue(Long fromId);

    Optional<MemberLog> findByFromIdAndToId(Long fromId, Long toId);

}
