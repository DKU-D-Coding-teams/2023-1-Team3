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
public class SignInResponse {

    private String grantType;

    private String accessToken;

    private String refreshToken;

    /*
        response에 JWT token 추가하여 반환하기
        private TokenResponse tokenResponse;
            -> AccessToken, RefreshToken 둘 다 담아두기
     */

//    public static SignInResponse of(Member member) {
//        return SignInResponse.builder()
//                .memberId(member.getMemberId())
//                .mail(member.getMail())
//                .build();
//    }

    public static SignInResponse of(JwtToken jwtToken) {
        return SignInResponse.builder()
                .grantType(jwtToken.getGrantType())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

}
