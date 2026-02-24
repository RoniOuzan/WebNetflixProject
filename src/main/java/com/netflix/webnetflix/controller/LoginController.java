package com.netflix.webnetflix.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping({"/", "/login"})
    public String showLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/series";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, HttpSession session) {
        if (username != null && !username.trim().isEmpty()) {
            session.setAttribute("username", username);
            return "redirect:/series";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}