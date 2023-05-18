package DKUDCoding20231Team3.VISTA.dto.response;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.database.MemberInterface;
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

    private String department;

    private String introduction;

    public static MemberResponse of(MemberInterface memberInterface) {
        return MemberResponse.builder()
                .memberId(memberInterface.getMemberId())
                .name(memberInterface.getName())
                .gender(memberInterface.getGender())
                .birth(memberInterface.getBirth())
                .image(memberInterface.getImage())
                .department(memberInterface.getDepartment())
                .introduction(memberInterface.getIntroduction())
                .build();
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .gender(member.getGender())
                .birth(member.getBirth())
                .image(member.getImage())
                .department(member.getDepartment())
                .introduction(member.getIntroduction())
                .build();
    }
}