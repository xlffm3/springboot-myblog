package com.glenn.myblog.service;

import com.glenn.myblog.domain.entity.Article;
import com.glenn.myblog.domain.exception.ArticleNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
    public void getAllArticles() {
        Article article = Article.builder()
                .id(1L)
                .authorName("Tester")
                .content("test")
                .build();
        List<Article> articles = Arrays.asList(article);

        when(articleRepository.findAll())
                .thenReturn(articles);

        List<ArticleDto> articleDtos = articleService.getAllArticles();
        ArticleDto articleDto = articleDtos.get(0);

        assertThat(articleDto.getAuthorName()).isEqualTo("Tester");
        assertThat(articleDto.getContent()).isEqualTo("test");
        verify(articleRepository, times(1)).findAll();
    }

    @DisplayName("ID로 특정 Article 조회")
    @Test
    public void getDetailArticleWithValidId() {
        Article article = Article.builder()
                .id(300L)
                .authorName("Tester")
                .content("test")
                .build();

        when(articleRepository.findById(300L))
                .thenReturn(Optional.of(article));

        ArticleDto articleDto = articleService.getDetailArticle(300L);

        assertThat(articleDto.getAuthorName()).isEqualTo("Tester");
        assertThat(articleDto.getContent()).isEqualTo("test");
        verify(articleRepository, times(1)).findById(eq(300L));
    }

    @DisplayName("참조하려는 Article ID가 없을 경우 예외 발생")
    @Test
    public void getDetailArticleWithInvalidId() {
        when(articleRepository.findById(300L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            articleService.getDetailArticle(300L);
        }).isInstanceOf(ArticleNotFoundException.class);
        verify(articleRepository, times(1)).findById(eq(300L));
    }

    @DisplayName("게시물 작성을 완료하면 Repository 추가 후 해당 ArticleDto 반환")
    @Test
    public void createArticle() {
        Article article = Article.builder()
                .id(1L)
                .authorName("Doe")
                .content("New")
                .build();

        when(articleRepository.save(any())).thenReturn(article);

        ArticleDto articleRequestDto = ArticleDto.of(article);
        ArticleDto articleResponseDto = articleService.createArticle(articleRequestDto);

        assertThat(articleResponseDto.getAuthorName()).isEqualTo("Doe");
        verify(articleRepository, times(1)).save(any());
    }
}
