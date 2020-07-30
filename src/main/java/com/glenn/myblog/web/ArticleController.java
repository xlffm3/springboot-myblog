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
    public String createArticle(@ModelAttribute ArticleDto articleRequestDto, Model model) {
        ArticleDto articleResponseDto = articleService.save(articleRequestDto);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @GetMapping("/{id}")
    public String readArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleResponseDto = articleService.findById(id);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String moveToEditPage(@PathVariable("id") Long id, Model model) {
        ArticleDto articleResponseDto = articleService.findById(id);
        model.addAttribute("article", articleResponseDto);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable("id") Long id, @ModelAttribute ArticleDto articleRequestDto, Model model) {
        ArticleDto articleResponseDto = articleService.update(id, articleRequestDto);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return "index";
    }
}
