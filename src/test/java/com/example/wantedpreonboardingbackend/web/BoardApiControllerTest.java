package com.example.wantedpreonboardingbackend.web;

import com.example.wantedpreonboardingbackend.domain.board.Article;
import com.example.wantedpreonboardingbackend.repository.BoardRepository;
import com.example.wantedpreonboardingbackend.web.dto.AddArticleRequestDto;
import com.example.wantedpreonboardingbackend.web.dto.UpdateArticleRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BoardApiControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BoardRepository boardRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        boardRepository.deleteAll();
    }

    @DisplayName("[api] 게시글 추가 테스트")
    @Test
    public void addArticle() throws Exception {
        // given
        final AddArticleRequestDto request = new AddArticleRequestDto("title", "content");
        final String requestBody = objectMapper.writeValueAsString((request));

        // when
        ResultActions result = mvc.perform(post("/api/latest/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = boardRepository.findAll();

        Assertions.assertThat(articles.size()).isEqualTo(1);
        Assertions.assertThat(articles.get(0).getTitle()).isEqualTo("title");
        Assertions.assertThat(articles.get(0).getContent()).isEqualTo("content");
    }

    @DisplayName("[api] 게시판 글 목록 조회 테스트")
    @Test
    public void findAllArticles() throws Exception {
        // given
        final String title = "title";
        final String content = "content";

        boardRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());
        // when
        final ResultActions result = mvc.perform(get("/api/latest/articles"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));
    }

    @DisplayName("[api] 게시글 단건 조회 테스트")
    @Test
    public void findByIdArticle() throws Exception {
        // given
        final String title = "title";
        final String content = "content";

        Article savedArticle = boardRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        // when
        final ResultActions result = mvc.perform(get("/api/latest/articles/{id}", savedArticle.getId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("[api] 게시글 삭제 테스트")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String title = "title";
        final String content = "content";

        Article savedArticle = boardRepository.save(
                Article.builder()
                        .title(title)
                        .content(content)
                        .build());

        // when
        mvc.perform(delete("/api/latest/articles/{id}", savedArticle.getId()))
                .andExpect(status().isOk());

        // then
        List<Article> articles = boardRepository.findAll();

        Assertions.assertThat(articles).isEmpty();
    }

    @DisplayName("[api] 게시글 수정 테스트")
    @Test
    public void updateArticle() throws Exception {
        // given
        Article savedArticle = boardRepository.save(
                Article.builder()
                        .title("title")
                        .content("content")
                        .build());

        UpdateArticleRequestDto requestDto = new UpdateArticleRequestDto("new title", "new content");

        // when
        ResultActions result = mvc.perform(put("/api/latest/articles/{id}", savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        result.andExpect(status().isOk());

        Article article = boardRepository.findById(savedArticle.getId()).get();

        Assertions.assertThat(article.getTitle()).isEqualTo("new title");
        Assertions.assertThat(article.getContent()).isEqualTo("new content");

    }
}