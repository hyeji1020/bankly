package com.project.bankassetor.service.perist;

import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("100명의 사용자를 생성하고 저장한다.")
    public void saveMembers() {
        List<Member> members = new ArrayList<>();
        for(int i = 1; i <= 100; i++){
            long id = i;
            String name = "test" + i;
            String email = "test" + i + "@gmail.com";
            String password = "password!" + i;
            String status = "active";
            String role = "user";
            String description = "test description" + i;
            LocalDateTime now = LocalDateTime.now();

            members.add(new Member(id, name, email, passwordEncoder.encode(password), status, role, description, now, now, now));
        }
        memberService.saveAll(members);

        long count = memberService.count(); // 데이터 개수 확인 메서드
        Assertions.assertEquals(100, count, "100명의 사용자가 저장되지 않았습니다.");
    }
}
