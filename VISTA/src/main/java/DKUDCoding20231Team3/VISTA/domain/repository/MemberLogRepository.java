package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {

    Optional<MemberLog> findByFromIdAndToId(Long fromId, Long toId);

}
