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
    @Column(name = "member_id")
    private Long memberId;

    private String refreshToken;

    public static RefreshToken of(Long memberId, String refreshToken) {
        return RefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .build();
    }

}
