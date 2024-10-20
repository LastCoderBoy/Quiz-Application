package com.Quiz.QuizApp.service;

import com.Quiz.QuizApp.models.Questions;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<List<Questions>> getAllQuestions();

    ResponseEntity<List<Questions>> getQuestionsBasedOnCategory(String categoryType);

    ResponseEntity<Questions> getQuestionByID(String questionID);

    ResponseEntity<String> addQuestion(Questions question);

    ResponseEntity<String> updateQuestion(String questionId, Questions newQuestion);

    ResponseEntity<String> deleteQuestion(String questionID);
}
