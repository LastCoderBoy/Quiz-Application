package com.microservice.question_microservice.service;

import com.microservice.question_microservice.models.QuestionWrapper;
import com.microservice.question_microservice.models.Questions;
import com.microservice.question_microservice.models.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    ResponseEntity<List<Questions>> getAllQuestions();

    ResponseEntity<List<Questions>> getQuestionsBasedOnCategory(String categoryType);

    //ResponseEntity<Questions> getQuestionByID(String questionID);

    ResponseEntity<String> addQuestion(Questions question);

    ResponseEntity<String> updateQuestion(Integer questionId, Questions newQuestion);

    ResponseEntity<String> deleteQuestion(Integer questionID);

    ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numOfQuestions);

    ResponseEntity<List<QuestionWrapper>> getQuestionsFromID(List<Integer> questionIDs);

    ResponseEntity<Integer> getQuestionScore(List<Response> responses);
}
