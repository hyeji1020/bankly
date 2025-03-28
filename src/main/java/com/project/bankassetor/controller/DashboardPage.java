package com.project.bankassetor.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardPage {

    @GetMapping("/admin/dashboard")
    public String showDashboardPage() {
        return "dashboard"; // templates/admin/dashboard.html
    }

}
