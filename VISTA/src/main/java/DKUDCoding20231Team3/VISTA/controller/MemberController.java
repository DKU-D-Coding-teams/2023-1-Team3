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
//import DKUDCoding20231Team3.VISTA.FilesUsedBefore.JwtAuthenticationFilterBefore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.*;
import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.INVALID_REQUEST_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;
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
    public ResponseEntity<SuggestResponse> suggest(HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);
//        return ResponseEntity.status(HttpStatus.OK).body(memberService.suggest(httpServletRequest));

        return ResponseEntity.status(HttpStatus.OK).body(memberService.suggest(member));
    }

    @PutMapping("/choice")
    public ResponseEntity<HttpStatus> choiceLike(@RequestParam("toId") Long toId,
                                                 @RequestParam("signal") Boolean signal,
                                                 HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);
        return ResponseEntity.status(memberService.choiceLike(toId, signal, member)).build();
    }

    @GetMapping("/likes")
    public ResponseEntity<LikeResponse> getLikes(@RequestParam("page") Integer page,
                                                 HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getLikes(page, member));
    }

    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                                    HttpServletRequest httpServletRequest) {
        Member member = findMemberByHttpServlet(httpServletRequest);
        return ResponseEntity.status(memberService.resetPassword(resetPasswordRequest, member)).build();
    }

//    private Member findMemberByHttpServlet(HttpServletRequest httpServletRequest) {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
//        final String token = jwtAuthenticationFilter.resolveToken(httpServletRequest);
//
////        if (token != null && token.startsWith("Bearer ")) {
////            String jwt = token.substring(7);
//        if (token != null) {
////                String jwt = token.substring(7);
//            if (jwtProvider.validateToken(token)) {
//                return memberRepository.findMemberByMail(jwtProvider.getMemberMail(token))
//                        .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
//            }
//            else
//                throw new VistaException(INVALID_ACCESS_TOKEN);
//        } else {
//            throw new VistaException(INVALID_REQUEST_TOKEN);
//        }
//    }

    private Member findMemberByHttpServlet(HttpServletRequest httpServletRequest) {
//        JwtAuthenticationFilterBefore jwtAuthenticationFilter = new JwtAuthenticationFilterBefore(jwtUtil);
//        final String token = jwtAuthenticationFilter.resolveToken(httpServletRequest);
        final String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);

//        if (token != null && token.startsWith("Bearer ")) {
//            String jwt = token.substring(7);
        if (accessToken != null) {
//                String jwt = token.substring(7);
            if (jwtUtil.validateToken(accessToken)) {
                return memberRepository.findMemberByMail(jwtUtil.getMailFromToken(accessToken))
                        .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));
            }
            else
                throw new VistaException(INVALID_ACCESS_TOKEN);
        } else {
            throw new VistaException(INVALID_REQUEST_TOKEN);
        }
    }

}
