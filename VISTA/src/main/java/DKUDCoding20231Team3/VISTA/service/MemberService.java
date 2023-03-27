package DKUDCoding20231Team3.VISTA.service;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.request.MailRequest;
import DKUDCoding20231Team3.VISTA.dto.request.MemberRequest;
import DKUDCoding20231Team3.VISTA.dto.response.MemberResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.util.MailUtil;
import DKUDCoding20231Team3.VISTA.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.ALREADY_SAVED_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;

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
        if(memberRepository.existsByMail(mailRequest.getMail())) throw new VistaException(ALREADY_SAVED_MEMBER);

        final String code = mailUtil.codeSend(mailRequest.getMail());
        redisUtil.setDataExpire(mailRequest.getMail(), code, 60000);

        return HttpStatus.CREATED;
    }
}
