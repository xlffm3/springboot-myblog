package com.glenn.myblog.web;

import com.glenn.myblog.dto.ArticleDto;
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
    public String moveToWritingPage(Model model) {
        model.addAttribute("articleDto", new ArticleDto());
        return "article-edit";
    }

    @GetMapping("/")
    public String moveToIndexPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
