package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.SuggestRefresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuggestRefreshRepository extends JpaRepository<SuggestRefresh, Long> {

    Optional<SuggestRefresh> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    @Transactional
    void deleteByMemberId(Long memberId);

}