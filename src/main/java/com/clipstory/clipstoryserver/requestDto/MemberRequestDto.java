package com.clipstory.clipstoryserver.requestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotNull
    @Schema(description = "아이디", example = "mingmingmon")
    private String customId;

    @NotNull
    @Schema(description = "비밀번호", example = "1234asdf!")
    private String password;

    @NotNull
    @Schema(description = "이름", example = "전민주")
    private String name;

}
