package DKUDCoding20231Team3.VISTA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class VistaExceptionHandler {

    @ExceptionHandler(VistaException.class)
    public ResponseEntity<VistaExceptionDto> vistaException(VistaException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(new VistaExceptionDto(ex.getErrorCode().getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<VistaExceptionDto> notFoundException() {
        final ErrorCode errorCode = ErrorCode.NOT_FOUND;
        return ResponseEntity.status(errorCode.getStatus())
                .body(new VistaExceptionDto(errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<VistaExceptionDto> validException() {
        final ErrorCode errorCode = ErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getStatus())
                .body(new VistaExceptionDto(errorCode.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    protected ResponseEntity<VistaExceptionDto> serverException(Exception ex) {
//        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
//        return ResponseEntity.status(errorCode.getStatus())
//                .body(new VistaExceptionDto(errorCode.getMessage()));
//    }

}
