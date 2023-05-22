package DKUDCoding20231Team3.VISTA.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SuggestResponse {

    private boolean endPageSignal;

    private int count;

    private List<MemberResponse> memberResponses;

    public static SuggestResponse of(boolean endPageSignal, int count, List<MemberResponse> memberResponses) {
        return SuggestResponse.builder()
                .endPageSignal(endPageSignal)
                .count(count)
                .memberResponses(memberResponses).build();
    }

}
