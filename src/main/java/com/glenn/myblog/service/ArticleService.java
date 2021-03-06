package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.Article;
import com.glenn.myblog.domain.exception.ArticleNotFoundException;
import com.glenn.myblog.domain.repository.ArticleRepository;
import com.glenn.myblog.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleDto> findAll() {
        return articleRepository.findAllByOrderByIdDesc()
                .stream()
                .map(ArticleDto::of)
                .collect(Collectors.toList());
    }

    public ArticleDto findById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleDto::of)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Transactional
    public ArticleDto save(ArticleDto articleRequestDto) {
        Article article = articleRequestDto.toEntity();
        return ArticleDto.of(articleRepository.save(article));
    }

    @Transactional
    public ArticleDto update(Long id, ArticleDto articleRequestDto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
        article.update(articleRequestDto.getTitle(), articleRequestDto.getAuthorName(), articleRequestDto.getContent());
        return ArticleDto.of(article);
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}
