package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberLogRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.*;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.util.JwtUtil.Jwt;
import DKUDCoding20231Team3.VISTA.util.JwtUtil.JwtAuthenticationFilter;
import DKUDCoding20231Team3.VISTA.util.JwtUtil.JwtProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
//@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public HttpStatus sendMail(MailRequest mailRequest) {
        if (memberRepository.existsByMail(mailRequest.getMail())) throw new VistaException(ALREADY_SAVED_MEMBER);

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

//    public SignInResponse signIn(SignInRequest signInRequest) {
//        final Member member = memberRepository.findByMail(signInRequest.getMail())
//                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
//
//        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
//            throw new VistaException(INVALID_PASSWORD);
//
//        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getMail());
//
//        return SignInResponse.of(jwtToken);
//    }

    @Transactional
    public SignInResponse signIn(String memberMail, String memberPassword) {

//        final Member member = memberRepository.findByMail(signInRequest.getMail())
//        .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
//
//        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
//            throw new VistaException(INVALID_PASSWORD);

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberMail, memberPassword);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        Jwt jwt = jwtProvider.generateToken(authentication);

        return SignInResponse.of(jwt);
    }

    @Transactional
    public SuggestResponse suggest(Member member) {
        final int SUGGEST_SIZE = 5;

//        final Member member = findMemberByHttpServlet(member);
        List<MemberListInterface> suggestMembers = memberRepository.getSuggestQuery(member.getMemberId(), member.getGender());

        boolean endPageSignal = false;
        Random random = new Random();
        List<MemberResponse> memberResponses = new ArrayList<>();
        for(int i = 0; i < SUGGEST_SIZE; i++) {
            if (suggestMembers.size() == 0) {
//                memberLogRepository.deleteByFromId(member.getMemberId());
                memberLogRepository.deleteByFromIdAndSignalIsTrue(member.getMemberId());
                endPageSignal = true;
                break;
            }
            int randomIndex = random.nextInt(suggestMembers.size());
            MemberListInterface randomMember = suggestMembers.get(randomIndex);
            suggestMembers.remove(randomMember);

            memberLogRepository.save(MemberLog.of(member.getMemberId(), randomMember.getMemberId(), false));
            memberResponses.add(MemberResponse.of(
                    randomMember.getMemberId(),
                    randomMember.getName(),
                    randomMember.getGender(),
                    randomMember.getBirth()
            ));
        }

        return SuggestResponse.of(endPageSignal, memberResponses);
    }

    public HttpStatus choiceLike(Long toId, Boolean signal, Member member) {
        MemberLog memberLog = memberLogRepository.findByFromIdAndToId(member.getMemberId(), toId)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER_LOG));

        memberLog.setSignal(signal);
        memberLogRepository.save(memberLog);

        return HttpStatus.OK;
    }

    public LikeResponse getLikes(Integer page, Member member) {
        final int LIKE_PAGE_SIZE = 3;
        List<MemberListInterface> likeMembers = memberRepository.getLikeQuery(
                member.getMemberId(), PageRequest.of(page, LIKE_PAGE_SIZE));

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

    public HttpStatus resetPassword(ResetPasswordRequest resetPasswordRequest, Member member) {
//        Member member = findMemberByHttpServlet(httpServletRequest);

        if(!passwordEncoder.matches(resetPasswordRequest.getCurrentPassword(), member.getPassword()))
            throw new VistaException(INVALID_PASSWORD);

        member.setPassword(passwordEncoder.encode(resetPasswordRequest.getFuturePassword()));
        memberRepository.save(member);

        return HttpStatus.OK;
    }

//    private Member findMemberByHttpServlet(HttpServletRequest httpServletRequest) {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
//        final String token = jwtAuthenticationFilter.resolveToken(httpServletRequest);
//
//        if (token != null && token.startsWith("Bearer ")) {
//            String jwt = token.substring(7);
//            if (jwtProvider.validateToken(jwt)) {
//                return memberRepository.findByMail(jwtProvider.getMemberMail(jwt))
//                        .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
//            }
//            else
//                throw new VistaException(INVALID_ACCESS_TOKEN);
//        } else {
//            throw new VistaException(INVALID_REQUEST_TOKEN);
//        }
//    }
    
}

//    public HttpStatus resetPassword(ResetPasswordRequest resetPasswordRequest, String mail); -> 이 방법으로!!
//    private Member findMemberByMail(String mail);