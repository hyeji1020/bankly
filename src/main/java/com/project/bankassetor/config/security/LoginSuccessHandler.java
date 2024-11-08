package com.project.bankassetor.config.security;

import com.project.bankassetor.primary.model.entity.Member;
import com.project.bankassetor.service.perist.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        Member member = (Member) authentication.getPrincipal();
        if (member.getStatus().equalsIgnoreCase("closed")) {
            String errorMessage = "잠긴계정입니다. 관리자에게 문의해주세요.";
            httpServletRequest.setAttribute("errMsg", errorMessage);
            throw new RuntimeException(errorMessage);
        }
        member.setLastLoginAt(LocalDateTime.now());
        memberService.save(member);
        httpServletRequest.getSession().setAttribute("member", member);

        // 로그인 성공 이력 저장

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}