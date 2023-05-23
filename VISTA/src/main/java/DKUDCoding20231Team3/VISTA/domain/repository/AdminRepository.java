package DKUDCoding20231Team3.VISTA.domain.repository;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Member, Long> {

    boolean existsByMail(String mail);

    Optional<Member> findByMail(String mail);

}


//public class Admin implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO)
//    @Column(name = "admin_id")
//    private Long memberId;
//
//    private String mail;
//    @Setter
//    private String password;
//
//    @ElementCollection(fetch = FetchType.EAGER)
//    @Builder.Default
//    private List<String> roles = new ArrayList<>();
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.roles.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public String getUsername() {
//        return mail;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
////    public static Admin of(SignUpRequest signUpRequest) {
////        return Admin.builder()
////                .mail(signUpRequest.getMail())
////                .password(signUpRequest.getPassword())
////                .build();
////    }
//}