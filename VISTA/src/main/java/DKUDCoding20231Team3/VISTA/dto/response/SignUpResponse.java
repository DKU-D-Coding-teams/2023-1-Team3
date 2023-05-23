package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NonNull
@AllArgsConstructor
public class SignUpResponse {

    private Long memberId;

    private String mail;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    public static SignUpResponse of(Member member) {
        return SignUpResponse.builder()
                .memberId(member.getMemberId())
                .mail(member.getMail())
                .name(member.getName())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();
    }

}
