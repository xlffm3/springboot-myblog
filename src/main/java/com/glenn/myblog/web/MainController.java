package com.glenn.myblog.web;

import com.glenn.myblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final ArticleService articleService;

    @GetMapping("/writing")
    public String moveToWritingPage() {
        return "article-edit";
    }

    @GetMapping("/")
    public String moveToIndexPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
