package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.repository.AdminRepository;
//import DKUDCoding20231Team3.VISTA.FilesUsedBefore.JwtProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
//    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;



}
