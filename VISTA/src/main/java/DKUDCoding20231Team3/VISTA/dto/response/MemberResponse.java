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

    private String image;

    public static MemberResponse of(Long memberId, String name, Gender gender, LocalDate birth, String image) {
        return MemberResponse.builder()
                .memberId(memberId)
                .name(name)
                .gender(gender)
                .birth(birth)
                .image(image)
                .build();
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .gender(member.getGender())
                .birth(member.getBirth())
                .image(member.getImage())
                .build();
    }
}