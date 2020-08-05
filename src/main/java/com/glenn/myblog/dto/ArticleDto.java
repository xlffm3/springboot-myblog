package com.glenn.myblog.dto;

import com.glenn.myblog.domain.entity.Article;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String authorName;
    @NotBlank
    private String content;
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
