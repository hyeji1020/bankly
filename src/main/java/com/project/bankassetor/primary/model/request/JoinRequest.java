package com.project.bankassetor.primary.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @Size(min = 2, max = 25, message = "이름은 2~25자 사이로 입력해주세요.")
    @NotBlank(message = "이름은 필수 입력입니다.")
    String name;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력입니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,}$",
            message = "비밀번호는 소문자, 숫자, 특수문자를 포함해야 합니다.")
    String password;

    String role;

    public JoinRequest(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
