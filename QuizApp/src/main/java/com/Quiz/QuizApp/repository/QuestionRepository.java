package com.Quiz.QuizApp.repository;

import com.Quiz.QuizApp.models.Questions;
import com.Quiz.QuizApp.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {
        @Query("SELECT t FROM Questions t WHERE t.category = :type")
        List<Questions> findQuestionsByCategory(String type);

        Questions findQuestionsById(Integer ID);

        @Query("SELECT q FROM Questions q WHERE q.category = :type ORDER BY RAND() LIMIT :limit")
        List<Questions> findQuestionsByCategoryLimited(String type, int limit);

}
