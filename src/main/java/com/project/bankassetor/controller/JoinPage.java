package com.project.bankassetor.controller;

import com.project.bankassetor.exception.BankException;
import com.project.bankassetor.primary.model.request.JoinRequest;
import com.project.bankassetor.service.perist.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinPage {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "join"; // 회원가입 페이지로 다시 이동
        }

        try {
            memberService.joinProcess(joinRequest);
        } catch (BankException ex) {
            // 에러 메시지를 모델에 추가하여 뷰로 전달
            model.addAttribute("emailError", ex.getMessage());
            return "join"; // 오류 발생 시 회원가입 페이지로 다시 이동
        }

        return "redirect:/login";
    }
}
