package DKUDCoding20231Team3.VISTA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class LikeResponse {

    private boolean endPageSignal;

    private List<MemberResponse> memberResponses;

    public static LikeResponse of(boolean endPageSignal, List<MemberResponse> memberResponses) {
        return builder()
                .endPageSignal(endPageSignal)
                .memberResponses(memberResponses).build();
    }

}
