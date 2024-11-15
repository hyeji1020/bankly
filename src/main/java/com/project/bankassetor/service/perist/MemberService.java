package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.exception.UserNotFoundException;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.model.request.JoinRequest;
import com.project.bankassetor.primary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(() -> {
            log.error("아이디 {}: 에 해당하는 사용자를 찾을 수 없습니다.", id);
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        });
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public void save(Member member){
        memberRepository.save(member);
    }

    public void joinProcess(JoinRequest joinRequest) {

        Optional<Member> isUser = memberRepository.findMemberByEmail(joinRequest.getEmail());
        if (isUser.isPresent()) {
            log.warn("{} :이미 가입되어 있는 이메일입니다.", joinRequest.getEmail());
            throw new BankException(ErrorCode.USER_EMAIL_DUPLICATE);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member data = new Member();
        data.setName(joinRequest.getName());
        data.setEmail(joinRequest.getEmail());
        data.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        data.setRole("ROLE_USER");
        data.setStatus("ACTIVE");

        memberRepository.save(data);
    }
}
