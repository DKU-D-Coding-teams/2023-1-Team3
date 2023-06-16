package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@NonNull
@AllArgsConstructor
public class BeforeSignInResponse {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    public static BeforeSignInResponse of(JwtToken jwtToken) {
        return BeforeSignInResponse.builder()
                .grantType(jwtToken.getGrantType())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

}
