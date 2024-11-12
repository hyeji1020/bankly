package com.project.bankassetor.primary.model.response;

import com.project.bankassetor.primary.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class JoinResponse {

    private long memberId;
    private String name;
    private String email;
    private String password;
    private String status;
    private LocalDateTime lastLoginAt;

    public static JoinResponse of(Member member){
        return JoinResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .status(member.getStatus())
                .lastLoginAt(member.getLastLoginAt())
                .build();
    }
}
