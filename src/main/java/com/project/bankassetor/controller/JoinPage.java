package com.project.bankassetor.controller;

import com.project.bankassetor.primary.model.request.JoinRequest;
import com.project.bankassetor.service.perist.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinPage {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join() {

        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(@ModelAttribute JoinRequest joinRequest) {
        System.out.println(joinRequest.getEmail());
        memberService.joinProcess(joinRequest);
        return "redirect:/login";
    }
}
