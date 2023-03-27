package DKUDCoding20231Team3.VISTA.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VistaException extends RuntimeException {

    private final ErrorCode errorCode;

}
