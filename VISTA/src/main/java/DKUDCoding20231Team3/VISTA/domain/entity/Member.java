package DKUDCoding20231Team3.VISTA.domain.entity;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "MEMBER_TABLE")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    private String mail;

    @Setter
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    @Setter
    private String image;

    private String department;

    private String introduction;

    public static Member of(SignUpRequest signUpRequest) {
        return Member.builder()
                .mail(signUpRequest.getMail())
                .password(signUpRequest.getPassword())
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .birth(signUpRequest.getBirth())
                .image("DEFAULT")
                .department(signUpRequest.getDepartment())
                .introduction(signUpRequest.getIntroduction())
                .build();
    }

    public void update(MyPageRequest myPageRequest) {
        this.name = myPageRequest.getName();
        this.gender = myPageRequest.getGender();
        this.birth = myPageRequest.getBirth();
        this.department = myPageRequest.getDepartment();
        this.introduction = myPageRequest.getIntroduction();
    }

}
