package DKUDCoding20231Team3.VISTA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@NonNull
@AllArgsConstructor
public class SignInResponse {

//    private String grantType;
//
//    private String accessToken;
//
//    private String refreshToken;
//
//    public static SignInResponse of(Jwt jwt) {
//        return SignInResponse.builder()
//                .grantType(jwt.getGrantType())
//                .accessToken(jwt.getAccessToken())
//                .refreshToken(jwt.getRefreshToken())
//                .build();
//    }
//
    private String accessToken;

    private String refreshToken;

    public static SignInResponse of(String accessToken, String refreshToken) {
        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
