package DKUDCoding20231Team3.VISTA.domain.entity;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.request.MemberRequest;
import DKUDCoding20231Team3.VISTA.dto.request.SignInRequest;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "MEMBER_TABLE")
//public class Member implements UserDetails {
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    private String mail;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public static Member of(MemberRequest memberRequest) {
        return Member.builder()
                .mail(memberRequest.getMail())
                .password(memberRequest.getPassword())
                .name(memberRequest.getName())
                .gender(memberRequest.getGender())
                .birth(memberRequest.getBirth())
                .build();
    }

    public static Member of(SignUpRequest signUpRequest) {
        return Member.builder()
                .mail(signUpRequest.getMail())
                .password(signUpRequest.getPassword())
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .birth(signUpRequest.getBirth())
                .build();
    }

    public static Member of(SignInRequest signInRequest) {
        return Member.builder()
                .mail(signInRequest.getMail())
                .password(signInRequest.getPassword())
                .build();
    }

    public static Member of(String mail, String password, String name, Gender gender, LocalDate birth) {
        return Member.builder()
                .mail(mail)
                .password(password)
                .name(name)
                .gender(gender)
                .birth(birth)
                .build();
    }

    public void updateMember(MemberRequest memberRequest) {
        this.mail = memberRequest.getMail();
        this.password = memberRequest.getPassword();
        this.name = memberRequest.getName();
        this.gender = memberRequest.getGender();
        this.birth = memberRequest.getBirth();
    }

}
