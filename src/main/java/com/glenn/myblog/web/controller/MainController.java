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
    public String moveToIndexPage(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String moveToWritingPage() {
        return "article-edit";
    }

    @GetMapping("/signup")
    public String moveToSignUpPage(@ModelAttribute UserDto userDto) {
        return "signup";
    }

    @GetMapping("/login")
    public String moveToLoginPage() {
        return "login";
    }
}
