package com.project.bankassetor.controller;

import com.project.bankassetor.config.security.Authed;
import com.project.bankassetor.primary.model.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainAdminPage {

    @GetMapping("/")
    public String adminMain(Model model, @Authed Member member) {
        model.addAttribute("member", member);
        model.addAttribute("message", "안녕하셔요 ???");
        return "index";
    }
}