package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
//@NonNull
@AllArgsConstructor
public class SuggestResponse {

    private List<MemberResponse> memberResponses;

    public static SuggestResponse of(List<MemberResponse> memberResponses) {
        return SuggestResponse.builder()
                .memberResponses(memberResponses)
                .build();
    }

}
