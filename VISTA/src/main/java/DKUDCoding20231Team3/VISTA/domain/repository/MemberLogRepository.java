package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {
    List<Long> findByFromId(Long fromId);

//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query("insert into MemberLog")
//    void saveFromIdAndToId(Long fromId, Long toId);
}
