package com.example.wantedpreonboardingbackend.repository;

import com.example.wantedpreonboardingbackend.domain.board.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Article, Long> {
}
