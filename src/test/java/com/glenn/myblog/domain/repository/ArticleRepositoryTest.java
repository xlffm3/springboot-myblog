package com.glenn.myblog.domain.repository;

import com.glenn.myblog.domain.entity.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @AfterEach
    public void deleteAll() {
        articleRepository.deleteAll();
    }

    @DisplayName("Article 생성 시 JPA Auditing을 통해 작성 및 수정 시간이 자동 저장")
    @Test
    public void auditCreatedDate() {
        LocalDateTime localDateTime = LocalDateTime.now();

        testEntityManager.persist(Article.builder()
                .title("title")
                .authorName("Tester")
                .content("Test")
                .build());
        Article article = articleRepository.findById(1L)
                .orElse(null);

        assertThat(article.getCreatedDate()).isAfter(localDateTime);
        assertThat(article.getModifiedDate()).isAfter(localDateTime);
    }
}
