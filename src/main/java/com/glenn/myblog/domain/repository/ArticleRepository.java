package com.glenn.myblog.domain.repository;

import com.glenn.myblog.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    public List<Article> findAllByOrderByIdDesc();
}
