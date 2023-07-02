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
    @Column(name = "suggest_refresh_id")
    private Long suggestRefreshId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Getter
    @Setter
    private boolean refreshSignal;

    public static SuggestRefresh of(Member member, boolean refreshSignal) {
        return SuggestRefresh.builder()
                .member(member)
                .refreshSignal(refreshSignal)
                .build();
    }
//    public static SuggestRefresh of(Long memberId, boolean refreshSignal) {
//        return SuggestRefresh.builder()
//                .memberId(memberId)
//                .refreshSignal(refreshSignal)
//                .build();
//    }

}
