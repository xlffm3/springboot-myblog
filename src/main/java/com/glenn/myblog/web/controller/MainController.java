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

    @GetMapping("/signup")
    public String moveToSignUpPage(HttpSession httpSession, @ModelAttribute UserDto userDto) {
        return isLogin(httpSession) ? "redirect:/" : "signup";
    }

    @GetMapping("/login")
    public String moveToLoginPage(HttpSession httpSession) {
        return isLogin(httpSession) ? "redirect:/" : "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if (isLogin(httpSession)) {
            httpSession.removeAttribute("loginDto");
        }
        return "redirect:/";
    }

    @GetMapping("/withdraw")
    public String moveToWithdrawPage(HttpSession httpSession) {
        return isLogin(httpSession) ? "withdraw" : "redirect:/login";
    }

    @GetMapping("/writing")
    public String moveToWritingPage(HttpSession httpSession) {
        return isLogin(httpSession) ? "article-edit" : "redirect:/login";
    }

    @GetMapping("/users")
    public String moveToUserListPage() {
        return "redirect:/users/list";
    }

    private boolean isLogin(HttpSession httpSession) {
        return httpSession.getAttribute("loginDto") != null;
    }
}
