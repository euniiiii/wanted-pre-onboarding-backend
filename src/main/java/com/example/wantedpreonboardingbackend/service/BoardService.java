package com.example.wantedpreonboardingbackend.service;

import com.example.wantedpreonboardingbackend.domain.board.Article;
import com.example.wantedpreonboardingbackend.repository.BoardRepository;
import com.example.wantedpreonboardingbackend.web.dto.AddArticleRequestDto;
import com.example.wantedpreonboardingbackend.web.dto.UpdateArticleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    // save
    public Article save(AddArticleRequestDto addArticleRequestDto) {
        return boardRepository.save(addArticleRequestDto.toEntity());
    }

    // all read
    public List<Article> findAll() {
        return boardRepository.findAll();
    }

    // read only 1
    public Article findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("article not exist! : " + id));
    }

    // delete
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // update
    @Transactional
    public Article update(long id, UpdateArticleRequestDto requestDto) {
        Article article = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("article not exist! : " + id));

        article.update(requestDto.getTitle(), requestDto.getContent());

        return article;
    }

}
