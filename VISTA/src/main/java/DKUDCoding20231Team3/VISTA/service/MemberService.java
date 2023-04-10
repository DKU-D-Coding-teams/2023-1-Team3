package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.entity.MemberLog;
import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberLogRepository;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.*;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.jwt.JwtToken;
import DKUDCoding20231Team3.VISTA.jwt.JwtTokenProvider;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

//    public MemberResponse create(MemberRequest memberRequest) {
//        final Member member = Member.of(memberRequest);
//        memberRepository.save(member);
//
//        return MemberResponse.of(member);
//    }
//
//    public DummyMembersResponse DummyMembers() {
//        Member[] memberList = new Member[10];
//
//        for (int i=0; i<10; i++) {
//            memberList[i] = Member.of(
//                String.valueOf(i+1),
//                String.valueOf(i+1),
//                String.valueOf(i+1),
//                Gender.MALE,
//                LocalDate.now()
//            );
//            memberRepository.save(memberList[i]);
//        }
//
//        return DummyMembersResponse.of(memberList);
//    }
//
//    public MemberResponse read(Long memberId) {
//        return MemberResponse.of(memberRepository.findById(memberId).orElseThrow());
//    }
//
//    public MemberResponse update(Long memberId, MemberRequest memberRequest) {
//        Member member = memberRepository.findById(memberId).orElseThrow();
//        member.updateMember(memberRequest);
//        memberRepository.save(member);
//
//        return MemberResponse.of(member);
//    }

    public HttpStatus delete(Long memberId) {
        memberRepository.deleteById(memberId);

        return HttpStatus.NO_CONTENT;
    }

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
                .orElseThrow(() -> new VistaException(UNAUTHORIZED_MAIL));

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new VistaException(INVALID_PASSWORD);
        }

        JwtToken jwtToken = jwtTokenProvider.generateToken(member.getMail());

        return SignInResponse.of(jwtToken);
    }

    public SuggestResponse suggest(HttpServletRequest httpServletRequest) {
//        Member requestedMember = findMemberByHttpServlet(httpServletRequest);

        long max = memberRepository.count();
        List<MemberResponse> memberResponses = new ArrayList<>();

        List temp = memberRepository.temp(1L);

        for (int i=0; i<5; i++) {
            Random random = new Random();
            int randomIndex = random.nextInt(temp.size());
            Object[] randomMember = (Object[]) temp.get(randomIndex);
            temp.remove(randomMember);
//            memberLogRepository.saveFromIdAndToId(1L, (Long)randomMember[0]);

            System.out.println("MemberID: " + randomMember[0]);
            System.out.println("Name: " + randomMember[1]);
            System.out.println("Gender: " + randomMember[2]);
            System.out.println("Birth: " + randomMember[3]);

            MemberResponse memberResponse = MemberResponse.of((Long)randomMember[0], (String)randomMember[1], (Gender)randomMember[2], (LocalDate)randomMember[3]);
            memberResponses.add(memberResponse);
        }

        /*
            1. A는 모든 유저 아이디 리스트
            2. A에서 내 아이디를 제외
            3. A에서 내가 이전에 봤던 사람들의 아이디를 제외
            4. A에서 5개의 아이디 랜덤 추출, 새로운 아이디 리스트 B에 저장
            5. B에 담긴 아이디를 이용하여 멤버 테이블에서 멤버 정보 추출
            6. 반환
         */

        return SuggestResponse.of(memberResponses);

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

    public HttpStatus makeTestCase() {
        List<Member> members = new ArrayList<>();

        for(int i = 0; i < 30; i++) {
            members.add(
                    Member.builder()
                            .mail(String.format("%08d@dankook.ac.kr", i))
                            .password("1q2w3e4r1!")
                            .name(String.format("%08dHGD", i))
                            .gender((i%2 != 0) ? Gender.FEMALE : Gender.MALE)
                            .birth(LocalDate.now())
                            .build()
            );
        }
        memberRepository.saveAll(members);

        List<MemberLog> memberLogs = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            memberLogs.add(
                    MemberLog.builder()
                            .fromId(1L)
                            .toId((long) ((i+1)*2))
                            .signal((i%2 != 0) ? true : false)
                            .build()
            );
        }
        memberLogRepository.saveAll(memberLogs);

        return HttpStatus.CREATED;

    }

}


/*
    /member/suggest

 */