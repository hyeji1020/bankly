package com.project.bankassetor.fixture;

import com.project.bankassetor.primary.model.entity.Member;

import java.time.LocalDateTime;

public class MemberFixture {

    public static Member get(){
        return Member.builder()
                .id(1L)
                .name("이혜지")
                .email("test@gmail.com")
                .password("test1234")
                .role("role_user")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .status("active")
                .description("")
                .build();
    }
}
