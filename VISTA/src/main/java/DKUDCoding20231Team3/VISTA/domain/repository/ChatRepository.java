package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Chat;
import DKUDCoding20231Team3.VISTA.dto.database.ChatInterface;
import DKUDCoding20231Team3.VISTA.dto.database.MemberInterface;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value =  "SELECT k.MESSAGE AS message, mtb.NAME AS name, mtb.GENDER AS gender, k.RECV_MEMBER_ID AS recvMemberId , k.TIME_STAMP AS timeStamp, k.ans AS ans, k.rn AS rn, mtb.IMAGE AS image " +
                    "FROM ( " +
                        "SELECT *, ROW_NUMBER() OVER(PARTITION BY j.ans ORDER BY j.TIME_STAMP DESC) AS rn " +
                        "FROM ( " +
                            "SELECT *, CASE WHEN i.RECV_MEMBER_ID = ?1 THEN i.SEND_MEMBER_ID ELSE i.RECV_MEMBER_ID END AS ans " +
                            "FROM CHAT_TABLE AS i " +
                            "WHERE i.RECV_MEMBER_ID = ?1 OR i.SEND_MEMBER_ID = ?1 ) as j ) as k " +
                    "INNER JOIN MEMBER_TABLE mtb ON k.ans = mtb.MEMBER_ID " +
                    "WHERE rn = 1"
    , nativeQuery = true)
    List<ChatInterface> getLastChatQuery(Long memberId);

    List<Chat> getChatsBySendMemberIdAndRecvMemberIdOrSendMemberIdAndRecvMemberId(
            Long sendMemberId_1,
            Long recvMemberId_1,
            Long sendMemberId_2,
            Long recvMemberId_2, Pageable pageable);

    boolean existsBySendMemberIdOrRecvMemberId(Long sendMemberId, Long recvMemberId);

    @Transactional
    void deleteBySendMemberIdOrRecvMemberId(Long sendMemberId, Long recvMemberId);
}
