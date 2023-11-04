package dev.example.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

    OK(200 , 200 , "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),

    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point"),

    PARAMETER_NO_VALIDATION(HttpStatus.BAD_REQUEST.value(), 400, "파라미터 값 검증 실패")
    ;

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;

}
