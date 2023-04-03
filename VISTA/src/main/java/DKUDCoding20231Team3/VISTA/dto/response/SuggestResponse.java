package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.ReturnMember;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@Builder
//@NonNull
@AllArgsConstructor
public class SuggestResponse {

    private ReturnMember[] returnMemberList;

    public static SuggestResponse of(ReturnMember[] returnMemberList) {
        return SuggestResponse.builder()
                .returnMemberList(returnMemberList)
                .build();
    }
}
