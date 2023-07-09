package DKUDCoding20231Team3.VISTA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
@NonNull
@AllArgsConstructor
public class SignOutResponse {

    private boolean signal;

    public static SignOutResponse of(boolean signal) {
        return SignOutResponse.builder()
                .signal(signal)
                .build();
    }
}
