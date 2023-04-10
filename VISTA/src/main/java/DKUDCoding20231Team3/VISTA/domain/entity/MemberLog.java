package DKUDCoding20231Team3.VISTA.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "MEMBER_LOG_TABLE")
public class MemberLog {

    @Id
    @GeneratedValue
    @Column(name = "member_log_id")
    private Long memberLogId;

    private Long fromId;

    private Long toId;

//    @ColumnDefault()
    private boolean signal;

    public static MemberLog of(Long fromId, Long toId, boolean signal) {
        return MemberLog.builder()
                .fromId(fromId)
                .toId(toId)
                .signal(signal)
                .build();
    }

}
