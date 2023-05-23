package DKUDCoding20231Team3.VISTA.controller;

import DKUDCoding20231Team3.VISTA.domain.repository.AdminRepository;
import DKUDCoding20231Team3.VISTA.service.AdminService;
//import DKUDCoding20231Team3.VISTA.FilesUsedBefore.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/")
public class AdminController {

    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;



}