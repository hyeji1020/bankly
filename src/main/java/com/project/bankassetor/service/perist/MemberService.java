package com.project.bankassetor.service.perist;

import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.exception.UserNotFoundException;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.primary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
