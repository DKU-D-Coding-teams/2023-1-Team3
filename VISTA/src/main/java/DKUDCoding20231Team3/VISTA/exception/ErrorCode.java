package DKUDCoding20231Team3.VISTA.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST
    INVALID_PARAMETER(400, "요구하는 형식에 맞지 않는 필드가 존재합니다."),

    //404 NOT_FOUND
    NOT_FOUND(404, "존재하지 않는 값입니다."),

    //409 CONFLICT
    ALREADY_SAVED_MEMBER(409, "이미 서비스에 가입된 계정입니다."),
    INVALID_MAIL_CODE(409, "유효하지 않은 인증코드 입니다"),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다.");

    private final int status;
    private final String message;

}
