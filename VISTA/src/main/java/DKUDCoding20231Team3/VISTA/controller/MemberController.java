package DKUDCoding20231Team3.VISTA.controller;

import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.*;
import DKUDCoding20231Team3.VISTA.exception.ErrorCode;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
import DKUDCoding20231Team3.VISTA.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    // without token

    @PostMapping("/mail")
    public ResponseEntity<HttpStatus> sendMail(@Valid @RequestBody MailRequest mailRequest) {
        String memberMail = mailRequest.getMail();

        return ResponseEntity.status(memberService.sendMail(memberMail)).build();
    }

    @PostMapping("/code")
    public ResponseEntity<HttpStatus> checkMail(@Valid @RequestBody MailCodeRequest mailCodeRequest) {
        String memberMail = mailCodeRequest.getMail();
        String memebrCode = mailCodeRequest.getCode();

        return ResponseEntity.status(memberService.checkMail(memberMail, memebrCode)).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        String memberMail = signInRequest.getMail();
        String memberPassword = signInRequest.getPassword();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(memberMail, memberPassword));
    }


    // with token

    @GetMapping("/suggest")
    public ResponseEntity<SuggestResponse> suggest() {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.suggest(memberMail));
    }

    @PutMapping("/choice")
    public ResponseEntity<HttpStatus> choiceLike(@RequestParam("toId") Long toId,
                                                 @RequestParam("likeSignal") Boolean likeSignal) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(memberService.choiceLike(toId, likeSignal, memberMail)).build();
    }

    @PutMapping("/block")
    public ResponseEntity<HttpStatus> choiceBlock(@RequestParam("toId") Long toId,
                                                  @RequestParam("blockSignal") Boolean blockSignal) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(memberService.choiceBlock(toId, blockSignal, memberMail)).build();
    }

    @GetMapping("/likes")
    public ResponseEntity<LikeResponse> getLikes(@RequestParam("page") Integer page) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getLikes(page, memberMail));
    }

    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(memberService.resetPassword(resetPasswordRequest, memberMail)).build();
    }

    @PostMapping("/images")
    public ResponseEntity<MemberResponse> uploadImage(@RequestParam("image") MultipartFile image)
            throws IOException, NoSuchAlgorithmException {
        if(image.getSize() == 0) throw new VistaException(ErrorCode.INVALID_MULTIPART_REQUEST);
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.uploadImage(image, memberMail));
    }

    @GetMapping("/mypages")
    public ResponseEntity<MemberResponse> getMyPages() {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMyPages(memberMail));
    }

    @PostMapping("/mypages")
    public ResponseEntity<MemberResponse> updateMyPages(@Valid @RequestBody MyPageRequest myPageRequest) {
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.updateMyPages(myPageRequest, memberMail));
    }

    @PostMapping("/signout")
    public ResponseEntity<SignOutResponse> signOut(@Valid @RequestBody SignOutRequest signOutRequest) {
        System.out.println("MemberController method signOut - checkpoint 1");
        String memberMail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberPassword = signOutRequest.getPassword();

        return ResponseEntity.status(HttpStatus.OK).body(memberService.signOut(memberMail, memberPassword));
    }

}
