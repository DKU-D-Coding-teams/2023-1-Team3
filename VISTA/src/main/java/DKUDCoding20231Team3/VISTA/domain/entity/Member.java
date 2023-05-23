package DKUDCoding20231Team3.VISTA.domain.entity;

//import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
//import DKUDCoding20231Team3.VISTA.dto.request.*;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//
//@Entity
//@Getter
//@Builder
//@AllArgsConstructor @NoArgsConstructor
//@Table(name = "MEMBER_TABLE")
//public class Member {
//
//    @Id
//    @GeneratedValue
//    @Column(name = "member_id")
//    private Long memberId;
//
//    private String mail;
//
//    @Setter
//    private String password;
//
//    private String name;
//
//    @Enumerated(EnumType.STRING)
//    private Gender gender;
//
//    private LocalDate birth;
//
//    public static Member of(SignUpRequest signUpRequest) {
//        return Member.builder()
//                .mail(signUpRequest.getMail())
//                .password(signUpRequest.getPassword())
//                .name(signUpRequest.getName())
//                .gender(signUpRequest.getGender())
//                .birth(signUpRequest.getBirth())
//                .build();
//    }
//
//}


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEMBER_TABLE")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "member_id")
    private Long memberId;

    private String mail;
    @Setter
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birth;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Builder.Default
//    private List<String> roles = new ArrayList<>();

//    @Transient
//    private Collection<SimpleGrantedAuthority> authorities;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    private String refreshToken;

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

//    public Role getRole() { return role; }

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

    public static Member of(SignUpRequest signUpRequest) {
        ArrayList<String> roles =  new ArrayList<>();
        roles.add("MEMBER");

        return Member.builder()
                .mail(signUpRequest.getMail())
                .password(signUpRequest.getPassword())
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .birth(signUpRequest.getBirth())
                .roles(roles)
                .build();
    }
}