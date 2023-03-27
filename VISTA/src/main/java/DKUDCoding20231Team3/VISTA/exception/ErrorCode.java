package DKUDCoding20231Team3.VISTA.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "요구하는 형식에 맞지 않는 필드가 존재합니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    NOT_FOUND(404, "존재하지 않는 값입니다."),

    //409 CONFLICT 중복된 리소스
    ALREADY_SAVED_MEMBER(409, "이미 서비스에 가입된 계정입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 내부 에러입니다.");

    private final int status;
    private final String message;

}
