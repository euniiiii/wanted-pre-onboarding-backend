package com.example.wantedpreonboardingbackend.web;

import com.example.wantedpreonboardingbackend.domain.board.Article;
import com.example.wantedpreonboardingbackend.service.BoardService;
import com.example.wantedpreonboardingbackend.web.dto.AddArticleRequestDto;
import com.example.wantedpreonboardingbackend.web.dto.ArticleResponseDto;
import com.example.wantedpreonboardingbackend.web.dto.UpdateArticleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping("/api/latest/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.save(requestDto));
    }

    @GetMapping("/api/latest/articles")
    public ResponseEntity<List<ArticleResponseDto>> findAllArticles() {
        List<ArticleResponseDto> articles = boardService.findAll()
                .stream()
                .map(ArticleResponseDto::new)
                .toList();
        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/latest/articles/{id}")
    public ResponseEntity<ArticleResponseDto> findArticle(@PathVariable long id) {
        Article article = boardService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponseDto(article));
    }

    @DeleteMapping("/api/latest/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        boardService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/api/latest/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequestDto requestDto) {
        Article updatedArticle = boardService.update(id, requestDto);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }

}
