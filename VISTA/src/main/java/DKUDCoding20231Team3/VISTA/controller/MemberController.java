package DKUDCoding20231Team3.VISTA.controller;

import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.LikeResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SuggestResponse;
import DKUDCoding20231Team3.VISTA.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("member/")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("mail")
    public ResponseEntity<HttpStatus> sendMail(@Valid @RequestBody MailRequest mailRequest) {
        return ResponseEntity.status(memberService.sendMail(mailRequest)).build();
    }

    @PostMapping("code")
    public ResponseEntity<HttpStatus> checkMail(@Valid @RequestBody MailCodeRequest mailCodeRequest) {
        return ResponseEntity.status(memberService.checkMail(mailCodeRequest)).build();
    }

    @PostMapping("signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(signUpRequest));
    }

    @PostMapping("signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(signInRequest));
    }

    @GetMapping("suggest")
    public ResponseEntity<SuggestResponse> suggest(HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.suggest(httpServletRequest));
    }

    @PutMapping("choice")
    public ResponseEntity<HttpStatus> choiceLike(@RequestParam("toId") Long toId,
                                                 @RequestParam("signal") Boolean signal,
                                                 HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(memberService.choiceLike(toId, signal, httpServletRequest)).build();
    }

    @GetMapping("likes")
    public ResponseEntity<LikeResponse> getLikes(@RequestParam("page") Integer page,
                                                 HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getLikes(page, httpServletRequest));
    }

    @PostMapping("reset")
    public ResponseEntity<HttpStatus> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                                    HttpServletRequest httpServletRequest) {
        return ResponseEntity.status(memberService.resetPassword(resetPasswordRequest, httpServletRequest)).build();
    }

    @PostMapping("images")
    public ResponseEntity<HttpStatus> uploadImage(@RequestParam("image") MultipartFile image,
                                                    HttpServletRequest httpServletRequest) throws IOException, NoSuchAlgorithmException {
        return ResponseEntity.status(memberService.uploadImage(image, httpServletRequest)).build();
    }

}
