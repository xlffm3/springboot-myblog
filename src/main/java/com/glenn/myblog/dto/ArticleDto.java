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
    private String authorName;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

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
