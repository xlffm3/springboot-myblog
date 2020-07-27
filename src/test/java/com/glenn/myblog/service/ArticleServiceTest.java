package com.glenn.myblog.service;

import com.glenn.myblog.domain.model.Article;
import com.glenn.myblog.domain.repository.ArticleRepository;
import com.glenn.myblog.dto.ArticleDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ArticleServiceTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("Article 전체 목록을 검색")
    @Test
    public void findAll() {
        Article article = Article.builder()
                .id(1L)
                .authorName("Tester")
                .content("test")
                .build();
        List<Article> articles = Arrays.asList(article);

        when(articleRepository.findAll())
                .thenReturn(articles);

        List<ArticleDto> articleDtos = articleService.getAllArticles();

        assertThat(articleDtos)
    }
    //TODO findAll (전체 목록 조회기때문에... 번호, 제목, 작성자명, 생성일자만)

    //TODO findById (이번에는 상세 내용이기때문에 내용까지 포함)

    //TODO POST (글 게시 요청)

}
