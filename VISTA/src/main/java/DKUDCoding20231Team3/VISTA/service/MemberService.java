package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberLogRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.database.MemberListInterface;
import DKUDCoding20231Team3.VISTA.dto.request.MailCodeRequest;
import DKUDCoding20231Team3.VISTA.dto.request.MailRequest;
import DKUDCoding20231Team3.VISTA.dto.request.ResetPasswordRequest;
import DKUDCoding20231Team3.VISTA.dto.request.SignUpRequest;
import DKUDCoding20231Team3.VISTA.dto.response.*;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.util.JwtUtil;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
//@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public HttpStatus sendMail(MailRequest mailRequest) {
        System.out.println("in sendMail - mailRequest: " + mailRequest);
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

    @Transactional
    public SignInResponse signIn(String memberMail, String memberPassword) {
        Authentication authentication = jwtUtil.generateAuthentication(memberMail, memberPassword);
        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(authentication);
        memberRepository.saveRefreshTokenByMail(memberMail, refreshToken);

        return SignInResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public SuggestResponse suggest(Member member) {
        System.out.println("MemberService method suggest - location check 1");
        final int SUGGEST_SIZE = 5;

//        final Member member = findMemberByHttpServlet(member);
        List<MemberListInterface> suggestMembers = memberRepository.getSuggestQuery(member.getMemberId(), member.getGender());

        boolean endPageSignal = false;
        Random random = new Random();
        List<MemberResponse> memberResponses = new ArrayList<>();
        for(int i = 0; i < SUGGEST_SIZE; i++) {
            if (suggestMembers.size() == 0) {
//                memberLogRepository.deleteByFromId(member.getMemberId());
//                memberLogRepository.deleteByFromIdAndSignalIsFalse(member.getMemberId());
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

//    public SignInResponse regenerateTokens();

    public SignInResponse regenerateTokens(String requestedRefreshToken) {
        String memberMail = jwtUtil.getMailFromToken(requestedRefreshToken);
        final Member member = memberRepository.findMemberByMail(memberMail)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
        Authentication authentication = jwtUtil.generateAuthentication(member.getMail(), member.getPassword());
        String newAccessToken = jwtUtil.generateAccessToken(authentication);
        String newRefreshToken = jwtUtil.generateRefreshToken(authentication);

        memberRepository.saveRefreshTokenByMail(memberMail, newRefreshToken);

        return SignInResponse.of(newAccessToken, newRefreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findMemberByMail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}
