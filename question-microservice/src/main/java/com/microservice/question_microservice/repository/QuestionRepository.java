package com.microservice.question_microservice.repository;

import com.microservice.question_microservice.models.QuestionWrapper;
import com.microservice.question_microservice.models.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Integer> {
        @Query("SELECT t FROM Questions t WHERE t.category = :type")
        List<Questions> findQuestionsByCategory(String type);

        Questions findQuestionsById(Integer ID);

        @Query("SELECT q.id FROM Questions q WHERE q.category = :type ORDER BY RAND() LIMIT :limit")
        List<Integer> findQuestionsByCategoryLimited(String type, int limit);

        @Query("SELECT new com.microservice.question_microservice.models.QuestionWrapper(q.id, q.questionTitle, q.option1, q.option2, q.option3, q.option4) " +
                "FROM Questions q WHERE q.id IN (:id)")
        List<QuestionWrapper> getQuestionsFromID(List<Integer> id);
}
