package DKUDCoding20231Team3.VISTA.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER_LOG_TABLE")
public class MemberLog {

    @Id
    @GeneratedValue
    @Column(name = "member_log_id")
    private Long memberLogId;

    private Long fromId;

    private Long toId;

    @Setter
    private boolean signal;

    public static MemberLog of(Long fromId, Long toId, boolean signal) {
        return MemberLog.builder()
                .fromId(fromId)
                .toId(toId)
                .signal(signal)
                .build();
    }

}
