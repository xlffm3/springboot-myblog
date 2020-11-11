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

    private Article article = Article.builder()
            .id(1L)
            .title("test article")
            .authorName("tester")
            .content("test content")
            .build();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("게시글 전체 조회")
    @Test
    public void findAll() {
        List<Article> articles = Arrays.asList(article);

        when(articleRepository.findAllByOrderByIdDesc()).thenReturn(articles);

        List<ArticleDto> articleDtos = articleService.findAll();
        ArticleDto articleDto = articleDtos.get(0);

        assertThat(articleDto.getAuthorName()).isEqualTo("tester");
        assertThat(articleDto.getContent()).isEqualTo("test content");
        verify(articleRepository, times(1)).findAllByOrderByIdDesc();
    }

    @DisplayName("ID로 특정 게시글 조회")
    @Test
    public void findById() {
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        ArticleDto articleDto = articleService.findById(1L);

        assertThat(articleDto.getAuthorName()).isEqualTo("tester");
        assertThat(articleDto.getContent()).isEqualTo("test content");
        verify(articleRepository, times(1)).findById(eq(1L));
    }

    @DisplayName("없는 ID로 특정 게시글 조회 시도시 예외 발생")
    @Test
    public void findById_예외() {
        when(articleRepository.findById(300L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            articleService.findById(300L);
        }).isInstanceOf(ArticleNotFoundException.class);
        verify(articleRepository, times(1)).findById(eq(300L));
    }

    @DisplayName("게시글 생성")
    @Test
    public void save() {
        when(articleRepository.save(any())).thenReturn(article);

        ArticleDto articleRequestDto = ArticleDto.of(article);
        ArticleDto articleResponseDto = articleService.save(articleRequestDto);

        assertThat(articleResponseDto.getAuthorName()).isEqualTo("tester");
        verify(articleRepository, times(1)).save(any());
    }

    @DisplayName("게시글 수정")
    @Test
    public void update() {
        ArticleDto requestDto = ArticleDto.builder()
                .id(1L)
                .title("test article")
                .authorName("tester")
                .content("after")
                .build();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        ArticleDto responseDto = articleService.update(1L, requestDto);

        assertThat(responseDto.getContent()).isEqualTo("after");
        verify(articleRepository, times(1)).findById(eq(1L));
    }

    @DisplayName("게시글 삭제")
    @Test
    public void delete() {
        articleService.deleteById(1L);

        verify(articleRepository, times(1)).deleteById(eq(1L));
    }
}
