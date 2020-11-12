package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.UserDto;
import com.glenn.myblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String moveToIndexPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String moveToWritingPage(HttpSession httpSession) {
        return "article-edit";
    }

    @GetMapping("/signup")
    public String moveToSignUpPage(@ModelAttribute UserDto userDto) {
        return "signup";
    }

    @GetMapping("/withdraw")
    public String moveToWithdrawPage(HttpSession httpSession) {
        return httpSession.getAttribute("loginDto") == null ? "redirect:/" : "/withdraw";
    }

    @GetMapping("/login")
    public String moveToLoginPage(HttpSession httpSession) {
        return httpSession.getAttribute("loginDto") == null ? "/login" : "redirect:/";
    }
}
