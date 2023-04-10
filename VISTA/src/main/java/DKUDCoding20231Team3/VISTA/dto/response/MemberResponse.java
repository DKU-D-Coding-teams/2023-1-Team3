package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    public static MemberResponse of(Long memberId, String name, Gender gender, LocalDate birth) {
        return MemberResponse.builder()
                .memberId(memberId)
                .name(name)
                .gender(gender)
                .birth(birth)
                .build();
    }

}