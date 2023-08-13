package com.example.wantedpreonboardingbackend.web.dto;

import com.example.wantedpreonboardingbackend.domain.board.Article;
import lombok.Getter;

@Getter
public class ArticleResponseDto {
    private String title;
    private String content;

    public ArticleResponseDto(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
