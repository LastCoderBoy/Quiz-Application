package com.quizApp.quiz_service.controller;

import com.quizApp.quiz_service.models.QuestionWrapper;
import com.quizApp.quiz_service.models.QuizDto;
import com.quizApp.quiz_service.models.Response;
import com.quizApp.quiz_service.service.impl.QuizServiceImpl;
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
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto) {
        return quizService.createQuiz(quizDto.getCategory(), quizDto.getNumberOfQuestions(), quizDto.getTitle());
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
