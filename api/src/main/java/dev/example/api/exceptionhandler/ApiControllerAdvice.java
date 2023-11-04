package dev.example.api.exceptionhandler;

import dev.example.api.common.api.Api;
import dev.example.api.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<Api<Object>> exception (BindException exception){
        log.error("",exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Api.ERROR(
                        ErrorCode.PARAMETER_NO_VALIDATION,
                        exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                );

    }



}
