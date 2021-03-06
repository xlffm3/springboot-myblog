package com.glenn.myblog.web.controller;

import com.glenn.myblog.dto.ArticleDto;
import com.glenn.myblog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public String createArticle(@Valid @ModelAttribute ArticleDto articleRequestDto) {
        ArticleDto articleResponseDto = articleService.save(articleRequestDto);
        return String.format("redirect:/articles/%d", articleResponseDto.getId());
    }

    @GetMapping("/{id}")
    public String readArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleResponseDto = articleService.findById(id);
        model.addAttribute("article", articleResponseDto);
        return "article";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable("id") Long id, @Valid @ModelAttribute ArticleDto articleRequestDto) {
        articleService.update(id, articleRequestDto);
        return String.format("redirect:/articles/%d", id);
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable("id") Long id) {
        articleService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String moveToEditPage(@PathVariable("id") Long id, Model model) {
        ArticleDto articleResponseDto = articleService.findById(id);
        model.addAttribute("article", articleResponseDto);
        return "article-edit";
    }
}
