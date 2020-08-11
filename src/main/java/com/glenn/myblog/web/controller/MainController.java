package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.UserDto;
import com.glenn.myblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String moveToWritingPage() {
        return "article-edit";
    }

    @GetMapping("/signup")
    public String moveToSignUpPage(Model model) {
        model.addAttribute("userDto", UserDto.builder().build());
        return "signup";
    }
}
