package DKUDCoding20231Team3.VISTA.controller;

import DKUDCoding20231Team3.VISTA.dto.request.*;
import DKUDCoding20231Team3.VISTA.dto.response.MemberResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.dto.response.SignUpResponse;
import DKUDCoding20231Team3.VISTA.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("member/")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("create")
    public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.create(memberRequest));
    }

    @GetMapping("read/{memberId}")
    public ResponseEntity<MemberResponse> read(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.read(memberId));
    }

    @PostMapping("update/{memberId}")
    public ResponseEntity<MemberResponse> update(@PathVariable Long memberId, @RequestBody MemberRequest memberRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.update(memberId, memberRequest));
    }

    @DeleteMapping("delete")
    public ResponseEntity<HttpStatus> delete(@RequestParam Long memberId) {
        return ResponseEntity.status(memberService.delete(memberId)).build();
    }

    @PostMapping("mail")
    public ResponseEntity<HttpStatus> sendMail(@Valid @RequestBody MailRequest mailRequest) {
        return ResponseEntity.status(memberService.sendMail(mailRequest)).build();
    }

    @PostMapping("code")
    public ResponseEntity<HttpStatus> checkMail(@Valid @RequestBody MailCodeRequest mailCodeRequest) {
        return ResponseEntity.status(memberService.checkMail(mailCodeRequest)).build();
    }

    @PostMapping("signUp")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(signUpRequest));
    }

    @PostMapping("signIn")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(signInRequest));
    }

}
