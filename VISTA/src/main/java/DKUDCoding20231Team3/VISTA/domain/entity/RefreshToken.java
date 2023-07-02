package DKUDCoding20231Team3.VISTA.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REFRESH_TOKEN_TABLE")
public class RefreshToken {
    @Id
    @GeneratedValue
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;
//    @Id
//    @GeneratedValue
//    @Column(name = "member_id")
//    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String refreshToken;

    public static RefreshToken of(Member member, String refreshToken) {
        return RefreshToken.builder()
                .member(member)
                .refreshToken(refreshToken)
                .build();
    }
//    public static RefreshToken of(String refreshToken) {
//        return RefreshToken.builder()
//                .refreshToken(refreshToken)
//                .build();
//    }

}
