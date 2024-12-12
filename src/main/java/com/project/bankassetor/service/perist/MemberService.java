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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TelegramNotificationService telegramNotificationService;

    public Member findById(Long id){
        return memberRepository.findById(id).orElseThrow(() -> {
            log.error("아이디 {}: 에 해당하는 사용자를 찾을 수 없습니다.", id);
            return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
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
            log.warn("회원가입 중: {} :이미 가입되어 있는 이메일입니다.", joinRequest.getEmail());
            throw new BankException(ErrorCode.USER_EMAIL_DUPLICATE);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member data = new Member();
        data.setName(joinRequest.getName());
        data.setEmail(joinRequest.getEmail());
        data.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        data.setRole("ROLE_USER");
        data.setStatus("active");

        memberRepository.save(data);

        String message = String.format(
                "%s님이 회원가입 하였습니다.\n가입 이메일: %s\n가입 시간: %s",
                data.getName(),
                data.getEmail(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        telegramNotificationService.sendTelegramMessage(message);
    }

    public Member findCheckByAccountId(long accountId) {
        
        return memberRepository.findCheckByAccountId(accountId).orElseThrow(() -> {
            log.error("입출금 계좌아이디 {}: 를 보유한 사용자를 찾을 수 없습니다.", accountId);
            return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        });
    }

    public Member findSaveByAccountId(long accountId) {

        return memberRepository.findSaveByAccountId(accountId).orElseThrow(() -> {
            log.error("적금 계좌아이디 {}: 를 보유한 사용자를 찾을 수 없습니다.", accountId);
            return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        });
    }

    public void saveAll(List<Member> members) {
        memberRepository.saveAll(members);
    }

    public long count() {
        return memberRepository.count();
    }
}
