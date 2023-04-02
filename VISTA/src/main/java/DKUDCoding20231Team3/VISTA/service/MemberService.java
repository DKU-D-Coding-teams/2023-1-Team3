package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import DKUDCoding20231Team3.VISTA.jwt.JwtTokenProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public HttpStatus sendMail(MailRequest mailRequest) {
        if (memberRepository.existsByMail(mailRequest.getMail()))
            throw new VistaException(ALREADY_SAVED_MEMBER);

        final String code = mailUtil.codeSend(mailRequest.getMail());
        redisUtil.setDataExpire(mailRequest.getMail(), code, 60000);

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

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        final Member member = Member.of(signUpRequest);
        memberRepository.save(member);

        return SignUpResponse.of(member);
    }

    public SignInResponse signIn(SignInRequest signInRequest) {
        final Member member = memberRepository.findByMail(signInRequest.getMail())
                .orElseThrow(() -> new VistaException(NOT_FOUND_MAIL));

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
            throw new VistaException(INVALID_PASSWORD);

        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getMail());

        return SignInResponse.of(jwtToken);
    }

}
