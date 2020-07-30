package com.glenn.myblog.web;

import com.glenn.myblog.dto.ArticleDto;
import com.glenn.myblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("")
    public String createPost(@ModelAttribute ArticleDto articleRequestDto, Model model) {
        ArticleDto articleResponseDto = articleService.save(articleRequestDto);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @GetMapping("/{id}")
    public String readPost(@PathVariable("id") Long id, Model model) {
        ArticleDto articleResponseDto = articleService.findById(id);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

}
