package com.clipstory.clipstoryserver.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum Status {

    //공통 정상 응답
    OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),

    //공통 오류 응답
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    CONFLICT(HttpStatus.CONFLICT, "COMMON409", "이미 생성되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버에 오류가 발생했습니다."),

    //Movie 오류 응답
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "MOVIE404", "존재하지 않는 영화입니다."),

    //Member 오류 응답
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "존재하지 않는 회원입니다."),
    MEMBER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER401", "회원 인증이 되지 않았습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "MEMBER409", "이미 존재하는 아이디입니다."),

    //JWT 오류 응답
    JWT_WRONG_TYPE_TOKEN(HttpStatus.BAD_REQUEST, "JWT400", "JWT 타입이 틀렸습니다."),
    JWT_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "JWT400", "JWT가 만료되었습니다."),
    JWT_NULL(HttpStatus.UNAUTHORIZED, "JWT401", "JWT가 NULL입니다."),
    JWT_INVALID(HttpStatus.FORBIDDEN, "JWT403", "JWT가 유효하지 않습니다."),

    //TAG 오류 응답
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "TAG404", "존재하지 않는 태그입니다."),

    //Rating 오류 응답
    RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "RATING404", "존재하지 않는 평점입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;

    public Body getBody() {
        return Body.builder()
                .message(message)
                .code(code)
                .isSuccess(httpStatus.is2xxSuccessful())
                .httpStatus(httpStatus)
                .build();
    }

}