package com.glenn.myblog.dto;

import com.glenn.myblog.domain.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static ArticleDto of(Article entity) {
        return ArticleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .authorName(entity.getAuthorName())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
    }

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .authorName(authorName)
                .build();
    }
}
