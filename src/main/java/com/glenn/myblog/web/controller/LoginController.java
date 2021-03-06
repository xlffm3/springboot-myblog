package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.LoginDto;
import com.glenn.myblog.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RequestMapping("/login")
@Controller
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public String login(HttpSession httpSession, @ModelAttribute LoginDto loginDto) {
        LoginDto loginResponseDto = loginService.login(loginDto);
        httpSession.setAttribute("loginDto", loginResponseDto);
        return "redirect:/";
    }
}
