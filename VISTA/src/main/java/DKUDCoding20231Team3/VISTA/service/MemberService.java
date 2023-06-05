package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.entity.SuggestRefresh;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberLogRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.SuggestRefreshRepository;
import DKUDCoding20231Team3.VISTA.dto.database.MemberInterface;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.*;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import DKUDCoding20231Team3.VISTA.jwt.JwtTokenProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.OCIUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberLogRepository memberLogRepository;
    private final SuggestRefreshRepository suggestRefreshRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;
    private final OCIUtil ociUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

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

    public SignInResponse signIn(SignInRequest signInRequest) {
        final Member member = memberRepository.findByMail(signInRequest.getMail())
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
            throw new VistaException(INVALID_PASSWORD);

        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getMail());

        return SignInResponse.of(jwtToken);
    }

    public SuggestResponse suggest(HttpServletRequest httpServletRequest) {
        final int SUGGEST_SIZE = 8;

        final Member member = findMemberByHttpServlet(httpServletRequest);

        SuggestRefresh suggestRefresh = suggestRefreshRepository.findByMemberId(member.getMemberId())
                .orElse(SuggestRefresh.of(member.getMemberId(), false));
        if(suggestRefresh.isRefreshSignal()) memberLogRepository.deleteByBlockSignalFalseAndLikeSignalFalseAndFromId(member.getMemberId());

        Random random = new Random();
        List<MemberResponse> memberResponses = new ArrayList<>();
        List<MemberLog> memberLogs = new ArrayList<>();
        List<MemberInterface> suggestMembers = memberRepository.getSuggestQuery(member.getMemberId(), member.getGender());

        if(suggestMembers.size() == 0) {
            memberLogRepository.deleteByBlockSignalFalseAndLikeSignalFalseAndFromId(member.getMemberId());
            suggestMembers = memberRepository.getSuggestQuery(member.getMemberId(), member.getGender());
            if(suggestMembers.size() == 0) return SuggestResponse.of(true, 0, memberResponses);
        }

        for(int i = 0; i < SUGGEST_SIZE; i++) {
            if(suggestMembers.size() == 0) break;

            int randomIndex = random.nextInt(suggestMembers.size());
            MemberInterface randomMember = suggestMembers.get(randomIndex);
            suggestMembers.remove(randomMember);
            memberLogs.add(MemberLog.of(member.getMemberId(), randomMember.getMemberId(), false, false));
            memberResponses.add(MemberResponse.of(randomMember));
        }
        memberLogRepository.saveAll(memberLogs);

        suggestRefresh.setRefreshSignal(memberResponses.size() < SUGGEST_SIZE);
        suggestRefreshRepository.save(suggestRefresh);

        return SuggestResponse.of(false, memberResponses.size(), memberResponses);
    }
    
    public HttpStatus choiceLike(Long toId, Boolean likeSignal, HttpServletRequest httpServletRequest) {
        MemberLog memberLog = memberLogRepository.findByFromIdAndToId(
                findMemberByHttpServlet(httpServletRequest).getMemberId(), toId)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER_LOG));

        memberLog.setLikeSignal(likeSignal);
        memberLogRepository.save(memberLog);

        return HttpStatus.OK;
    }

    public HttpStatus choiceBlock(Long toId, Boolean blockSignal, HttpServletRequest httpServletRequest) {
        MemberLog memberLog = memberLogRepository.findByFromIdAndToId(
                        findMemberByHttpServlet(httpServletRequest).getMemberId(), toId)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER_LOG));

        memberLog.setBlockSignal(blockSignal);
        memberLogRepository.save(memberLog);

        return HttpStatus.OK;
    }

    public LikeResponse getLikes(Integer page, HttpServletRequest httpServletRequest) {
        final int LIKE_PAGE_SIZE = 8;
        List<MemberInterface> likeMembers = memberRepository.getLikeQuery(
                findMemberByHttpServlet(httpServletRequest).getMemberId(), PageRequest.of(page, LIKE_PAGE_SIZE));

        boolean endPageSignal = likeMembers.size() < LIKE_PAGE_SIZE;
        List<MemberResponse> memberResponses = new ArrayList<>();
        for(MemberInterface likeMember : likeMembers) memberResponses.add(MemberResponse.of(likeMember));

        return LikeResponse.of(endPageSignal, memberResponses.size(), memberResponses);
    }

    public HttpStatus resetPassword(ResetPasswordRequest resetPasswordRequest, HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);

        if(!passwordEncoder.matches(resetPasswordRequest.getCurrentPassword(), member.getPassword()))
            throw new VistaException(INVALID_PASSWORD);

        member.setPassword(passwordEncoder.encode(resetPasswordRequest.getFuturePassword()));
        memberRepository.save(member);

        return HttpStatus.OK;
    }

    public MemberResponse uploadImage(MultipartFile image, HttpServletRequest httpServletRequest) throws IOException, NoSuchAlgorithmException {
        Member member = findMemberByHttpServlet(httpServletRequest);

        member.setImage(ociUtil.putImage(member.getMail(), image));
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    public MemberResponse getMyPages(HttpServletRequest httpServletRequest) {
        return MemberResponse.of(findMemberByHttpServlet(httpServletRequest));
    }

    public MemberResponse updateMyPages(MyPageRequest myPageRequest, HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);
        member.update(myPageRequest);
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    private Member findMemberByHttpServlet(HttpServletRequest httpServletRequest) {
        final String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (jwtTokenProvider.validateToken(jwt)) {
                return memberRepository.findByMail(jwtTokenProvider.getSubject(jwt))
                        .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
            }
            else
                throw new VistaException(INVALID_ACCESS_TOKEN);
        } else {
            throw new VistaException(INVALID_REQUEST_TOKEN);
        }
    }
    
}
