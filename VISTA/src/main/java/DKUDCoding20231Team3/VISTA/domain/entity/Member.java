package DKUDCoding20231Team3.VISTA.domain.entity;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.request.MyPageRequest;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "MEMBER_TABLE")
public class Member implements UserDetails {

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
                .image("DEFAULT")
                .department(signUpRequest.getDepartment())
                .introduction(signUpRequest.getIntroduction())
                .roles(new ArrayList<>(Arrays.asList("MEMBER")))
                .build();
    }

    public void update(MyPageRequest myPageRequest) {
        this.name = myPageRequest.getName();
        this.gender = myPageRequest.getGender();
        this.birth = myPageRequest.getBirth();
        this.department = myPageRequest.getDepartment();
        this.introduction = myPageRequest.getIntroduction();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
