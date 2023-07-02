package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Long memberId);
//    Optional<RefreshToken> findByMemberId(Long memberId);

    @Transactional
    @Modifying
    @Query(value = "update RefreshToken as r set r.refreshToken = ?2 where r.member = ?1")
    void saveRefreshTokenByMail(Long memberId, String refreshToken);

}
