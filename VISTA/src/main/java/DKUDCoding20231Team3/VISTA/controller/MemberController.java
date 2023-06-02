package DKUDCoding20231Team3.VISTA.controller;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.LikeResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SuggestResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.service.MemberService;
import DKUDCoding20231Team3.VISTA.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.INVALID_PASSWORD;
import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.NOT_FOUND_MEMBER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/mail")
    public ResponseEntity<HttpStatus> sendMail(@Valid @RequestBody MailRequest mailRequest) {
        return ResponseEntity.status(memberService.sendMail(mailRequest)).build();
    }

    @PostMapping("/code")
    public ResponseEntity<HttpStatus> checkMail(@Valid @RequestBody MailCodeRequest mailCodeRequest) {
        return ResponseEntity.status(memberService.checkMail(mailCodeRequest)).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        final Member member = memberRepository.findMemberByMail(signInRequest.getMail())
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword()))
            throw new VistaException(INVALID_PASSWORD);

        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(member.getMail(), member.getPassword()));

    }

    @GetMapping("/test")
    public String test() {
        System.out.println("test success");
        return "sucess";
    }

    @GetMapping("/suggest")
    public ResponseEntity suggest(HttpServletRequest httpServletRequest) {
        String requestedAccessToken = httpServletRequest.getHeader("Access_Token");
        String requestedRefreshToken = httpServletRequest.getHeader("Refresh_Token");

        if (requestedAccessToken == null && requestedRefreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberService.regenerateTokens(requestedRefreshToken));
        }

        Member member = memberRepository.findMemberByMail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        return ResponseEntity.status(HttpStatus.OK).body(memberService.suggest(member));
    }

    @PutMapping("/choice")
    public ResponseEntity choiceLike(@RequestParam("toId") Long toId,
                                                 @RequestParam("signal") Boolean signal,
                                                 HttpServletRequest httpServletRequest) {
        String requestedAccessToken = httpServletRequest.getHeader("Access_Token");
        String requestedRefreshToken = httpServletRequest.getHeader("Refresh_Token");

        if (requestedAccessToken == null && requestedRefreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberService.regenerateTokens(requestedRefreshToken));
        }

        Member member = findMemberByHttpServletAccessToken(httpServletRequest);
        return ResponseEntity.status(memberService.choiceLike(toId, signal, member)).build();
    }

    @GetMapping("/likes")
    public ResponseEntity getLikes(@RequestParam("page") Integer page,
                                                 HttpServletRequest httpServletRequest) {
        String requestedAccessToken = httpServletRequest.getHeader("Access_Token");
        String requestedRefreshToken = httpServletRequest.getHeader("Refresh_Token");

        if (requestedAccessToken == null && requestedRefreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberService.regenerateTokens(requestedRefreshToken));
        }

        Member member = findMemberByHttpServletAccessToken(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getLikes(page, member));
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                                    HttpServletRequest httpServletRequest) {
        String requestedAccessToken = httpServletRequest.getHeader("Access_Token");
        String requestedRefreshToken = httpServletRequest.getHeader("Refresh_Token");

        if (requestedAccessToken == null && requestedRefreshToken != null) {
            return ResponseEntity.status(HttpStatus.OK).body(memberService.regenerateTokens(requestedRefreshToken));
        }

        Member member = findMemberByHttpServletAccessToken(httpServletRequest);
        return ResponseEntity.status(memberService.resetPassword(resetPasswordRequest, member)).build();
    }

    private Member findMemberByHttpServletAccessToken(HttpServletRequest httpServletRequest) {
        final String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);

        return memberRepository.findMemberByMail(jwtUtil.getMailFromToken(accessToken))
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
    }

    private Member findMemberByHttpServletRefreshToken(HttpServletRequest httpServletRequest) {
        final String refreshToken = jwtUtil.getRefreshTokenFromHeader(httpServletRequest);

        return memberRepository.findMemberByMail(jwtUtil.getMailFromToken(refreshToken))
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
    }

    private Member findMemberByAuthentication(Authentication authentication) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("MemberController method findMemberByAuthentication - memberMail: " + memberMail);

        return memberRepository.findMemberByMail(memberMail)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
    }

//    private ResponseEntity regenerateTokens(String requestedRefreshToken) {
//        String mail = jwtUtil.getMailFromToken(requestedRefreshToken);
//        final Member member = memberRepository.findMemberByMail(mail)
//                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
//        Authentication authentication = jwtUtil.generateAuthentication(member.getMail(), member.getPassword());
//        String newAccessToken = jwtUtil.generateAccessToken(authentication);
//        String newRefreshToken = jwtUtil.generateRefreshToken(authentication);
//
//        return ResponseEntity.status(HttpStatus.OK).body(SignInResponse.of(newAccessToken, newRefreshToken));
//    }
}
