package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberLogRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.LikeResponse;
import DKUDCoding20231Team3.VISTA.dto.response.MemberResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import DKUDCoding20231Team3.VISTA.jwt.JwtTokenProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;
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

    public HttpStatus choiceLike(Long toId, Boolean signal, HttpServletRequest httpServletRequest) {
        MemberLog memberLog = memberLogRepository.findByFromIdAndToId(
                findMemberByHttpServlet(httpServletRequest).getMemberId(), toId)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER_LOG));

        memberLog.setSignal(signal);
        memberLogRepository.save(memberLog);

        return HttpStatus.OK;
    }

    public LikeResponse getLikes(Integer page, HttpServletRequest httpServletRequest) {
        final int LIKE_PAGE_SIZE = 3;
        List<MemberListInterface> likeMembers = memberRepository.getLikeQuery(
                findMemberByHttpServlet(httpServletRequest).getMemberId(), PageRequest.of(page, LIKE_PAGE_SIZE));

        boolean endPageSignal = likeMembers.size() < LIKE_PAGE_SIZE;
        List<MemberResponse> memberResponses = new ArrayList<>();
        for(MemberListInterface likeMember : likeMembers) {
            memberResponses.add(MemberResponse.of(
                    likeMember.getMemberId(),
                    likeMember.getName(),
                    likeMember.getGender(),
                    likeMember.getBirth()
            ));
        }

        return LikeResponse.of(endPageSignal, memberResponses);
    }

    private Member findMemberByHttpServlet(HttpServletRequest httpServletRequest) {
        final String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (jwtTokenProvider.validateToken(jwt)) {
                return memberRepository.findByMail(jwtTokenProvider.getSubject(jwt))
                        .orElseThrow(() -> new VistaException(NOT_FOUND_MAIL));
            }
            else
                throw new VistaException(INVALID_ACCESS_TOKEN);
        } else {
            throw new VistaException(INVALID_REQUEST_TOKEN);
        }
    }
}
