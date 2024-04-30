package com.clipstory.clipstoryserver.global.response;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unexpectedException(
            Exception unexpectedException,
            WebRequest webRequest
    ) {
        log.error("예상치 못한 오류 발생: {}", unexpectedException.getMessage());
        log.error("발생 지점: {}", unexpectedException.getStackTrace()[0]);
        Body body = Status.INTERNAL_SERVER_ERROR.getBody();
        ApiResponse<Object> response = ApiResponse.onFailure(body.getCode(), body.getMessage(), null);
        return super.handleExceptionInternal(
                unexpectedException,
                response,
                HttpHeaders.EMPTY,
                HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest
        );
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> exception(
            GeneralException generalException,
            HttpServletRequest request
    ) {
        Body body = generalException.getBody();
        ApiResponse<Object> response = ApiResponse.onFailure(body.getCode(), body.getMessage(), null);
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                generalException,
                response,
                HttpHeaders.EMPTY,
                body.getHttpStatus(),
                webRequest
        );
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();
        Body body = Status.BAD_REQUEST.getBody();

        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage())
                            .orElse("");
                    errors.merge(fieldName, errorMessage,
                            (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", "
                                    + newErrorMessage);
                });

        ApiResponse<Object> response = ApiResponse.onFailure(body.getCode(), body.getMessage(),
                errors);

        return super.handleExceptionInternal(
                exception,
                response,
                headers,
                Status.BAD_REQUEST.getHttpStatus(),
                request
        );
    }

}

