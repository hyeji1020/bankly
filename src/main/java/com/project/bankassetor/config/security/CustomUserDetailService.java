package com.project.bankassetor.config.security;

import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.exception.ErrorCode;
import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.service.perist.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberOptional = memberService.findMemberByEmail(username);
        if (memberOptional.isEmpty()) {
            log.error("아이디 {}: 에 해당하는 사용자를 찾을 수 없습니다.", username);
            throw new BankException(ErrorCode.USER_NOT_FOUND);
        }

        return memberOptional.get();
    }

}