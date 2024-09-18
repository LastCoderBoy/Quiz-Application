package com.quizApp.quiz_service.feign;

import com.quizApp.quiz_service.models.QuestionWrapper;
import com.quizApp.quiz_service.models.Questions;
import com.quizApp.quiz_service.models.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("QUESTION-MICROSERVICE")
public interface FromQuestionService {
    @GetMapping("question/category/{questionType}")
    public ResponseEntity<List<Questions>> getQuestionsBasedOnCategory(@PathVariable String questionType);

    @GetMapping("question/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam Integer numOfQuestions);

    @PostMapping("question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromID(@RequestBody List<Integer> questionIDs);

    @PostMapping("question/getScore")
    public ResponseEntity<Integer> getQuestionScore(@RequestBody List<Response> responses);
}
