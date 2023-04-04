package DKUDCoding20231Team3.VISTA.domain.entity;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.request.SignInRequest;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Like> likes;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

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

}
