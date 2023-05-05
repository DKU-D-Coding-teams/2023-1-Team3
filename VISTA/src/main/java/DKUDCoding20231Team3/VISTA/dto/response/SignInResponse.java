package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.util.JwtUtil.Jwt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@NonNull
@AllArgsConstructor
public class SignInResponse {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    public static SignInResponse of(Jwt jwtToken) {
        return SignInResponse.builder()
                .grantType(jwtToken.getGrantType())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

}
