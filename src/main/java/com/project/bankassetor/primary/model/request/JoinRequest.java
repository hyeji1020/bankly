package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "이름 입력은 필수 입니다.")
    String name;

    @NotBlank(message = "이메일 입력은 필수 입니다.")
    String email;

    @NotBlank(message = "비밀번호 입력은 필수 입니다.")
    String password;

    String role;

}
