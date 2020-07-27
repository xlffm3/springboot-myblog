package com.glenn.myblog.service;

import com.glenn.myblog.domain.repository.ArticleRepository;
import com.glenn.myblog.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleDto> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(ArticleDto::of)
                .collect(Collectors.toList());
    }
}
