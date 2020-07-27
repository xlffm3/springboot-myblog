package com.glenn.myblog.dto;

import com.glenn.myblog.domain.entity.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
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
}
