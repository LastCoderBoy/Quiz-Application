package com.quizApp.quiz_service.service;

import com.quizApp.quiz_service.models.QuestionWrapper;
import com.quizApp.quiz_service.models.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface QuizService {
    ResponseEntity<String> createQuiz(String category, int numOfQuestions, String title);
    ResponseEntity<List<QuestionWrapper>> getQuizQuestionsByID(int id);
    ResponseEntity<Integer> calculateResult(int id, List<Response> userResponses);
}
