package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {

    @Transactional
    void deleteByBlockSignalFalseAndLikeSignalFalseAndFromMember(Long fromId);
//    @Transactional
//    void deleteByBlockSignalFalseAndLikeSignalFalseAndFromId(Long fromId);

    Optional<MemberLog> findByFromMemberAndToMember(Long fromId, Long toId);
//    Optional<MemberLog> findByFromIdAndToId(Long fromId, Long toId);

}
