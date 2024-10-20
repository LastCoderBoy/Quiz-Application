package com.Quiz.QuizApp.controller;

import com.Quiz.QuizApp.models.QuestionWrapper;
import com.Quiz.QuizApp.models.Response;
import com.Quiz.QuizApp.service.impl.QuizServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("quiz")
public class QuizController {

    private final QuizServiceImpl quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category,
                                             @RequestParam int numOfQuestions,
                                             @RequestParam String title) {
        return quizService.createQuiz(category, numOfQuestions, title);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizById(@PathVariable int id) {
        return quizService.getQuizQuestionsByID(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> userResponses) {
        return quizService.calculateResult(id, userResponses);
    }
}
