package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.MemberResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import DKUDCoding20231Team3.VISTA.jwt.JwtTokenProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final MailUtil mailUtil;

    private final RedisUtil redisUtil;

//    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final JwtTokenProvider jwtTokenProvider;

//    private final PasswordEncoder passwordEncoder;

    public MemberResponse create(MemberRequest memberRequest) {
        final Member member = Member.of(memberRequest);
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    public MemberResponse read(Long memberId) {
        return MemberResponse.of(memberRepository.findById(memberId).orElseThrow());
    }

    public MemberResponse update(Long memberId, MemberRequest memberRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.updateMember(memberRequest);
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    public HttpStatus delete(Long memberId) {
        memberRepository.deleteById(memberId);

        return HttpStatus.NO_CONTENT;
    }

    public HttpStatus sendMail(MailRequest mailRequest) {
        if (memberRepository.existsByMail(mailRequest.getMail())) throw new VistaException(ALREADY_SAVED_MEMBER);

        System.out.println("send mail check1");
        final String code = mailUtil.codeSend(mailRequest.getMail());
        System.out.println("send mail check2");
        redisUtil.setDataExpire(mailRequest.getMail(), code, 60000);
        System.out.println("send mail check3");

        return HttpStatus.CREATED;
    }

    public HttpStatus checkMail(MailCodeRequest mailCodeRequest) {
        final String code = redisUtil.getData(mailCodeRequest.getMail());
        if (code == null || !code.equals(mailCodeRequest.getCode()))
            throw new VistaException(INVALID_MAIL_CODE);

        redisUtil.deleteData(mailCodeRequest.getMail());
        redisUtil.setDataExpire(mailCodeRequest.getMail(), "OK", 60000);

        return HttpStatus.CREATED;
    }

    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        final String code = redisUtil.getData(signUpRequest.getMail());

        if(code == null || !code.equals("OK"))
            throw new VistaException(UNAUTHORIZED_MAIL);

        /*
            singUpRequest.getPassword() -> encrypt
         */

        final Member member = Member.of(signUpRequest);

        memberRepository.save(member);

        return SignUpResponse.of(member);
    }

    public SignInResponse signIn(SignInRequest signInRequest) {

//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequest.getMail(), signInRequest.getPassword());
//        Authentication authenication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        JwtToken jwtToken = jwtTokenProvider.generateToken(authenication);

//        return SignInResponse.of(jwtToken);

        final Member member = memberRepository.findByMail(signInRequest.getMail())
                .orElseThrow(IllegalArgumentException::new);
        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getMail());

        return SignInResponse.of(jwtToken);
    }
//
//    public String createToken(LoginRequest loginRequest) {
//        User user = userRepository.findByName(loginRequest.getName())
//                .orElseThrow(IllegalArgumentException::new);
//        //비밀번호 확인 등의 유효성 검사 진행
//        return jwtTokenProvider.createToken(user.getName());
//    }

}