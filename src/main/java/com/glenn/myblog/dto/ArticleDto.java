package com.glenn.myblog.dto;

import com.glenn.myblog.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ArticleDto(Long id, String authorName, String content) {
        this.id = id;
        this.authorName = authorName;
        this.content = content;
    }

    public static ArticleDto of(Article entity) {
        return ArticleDto.builder()
                .id(entity.getId())
                .authorName(entity.getAuthorName())
                .content(entity.getContent())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }

    public Article toEntity() {
        return Article.builder()
                .id(id)
                .authorName(authorName)
                .content(content)
                .build();
    }
}
