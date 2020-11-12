package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.LoginDto;
import com.glenn.myblog.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String login(HttpSession httpSession, @ModelAttribute LoginDto loginDto) {
        LoginDto responseDto = loginService.login(loginDto);
        httpSession.setAttribute("loginDto", responseDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if (httpSession.getAttribute("loginDto") == null) {
            return "redirect:/";
        }
        httpSession.removeAttribute("loginDto");
        return "/index";
    }
}
