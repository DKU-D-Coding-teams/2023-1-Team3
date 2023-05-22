package DKUDCoding20231Team3.VISTA.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUGGEST_REFRESH_TABLE")
public class SuggestRefresh {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Getter
    @Setter
    private boolean refreshSignal;

    public static SuggestRefresh of(Long memberId, boolean refreshSignal) {
        return SuggestRefresh.builder()
                .memberId(memberId)
                .refreshSignal(refreshSignal)
                .build();
    }

}
