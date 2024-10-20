package com.Quiz.QuizApp.controller;

import com.Quiz.QuizApp.models.Questions;
import com.Quiz.QuizApp.service.impl.QuestionServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("question")
public class QuestionController {

    private final QuestionServiceImpl questionService;

    @GetMapping("allQuestions")
    public ResponseEntity<List<Questions>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("category/{questionType}")
    public Object getQuestionsBasedOnCategory(@PathVariable String questionType){
        ResponseEntity<List<Questions>> questionsBasedOnCategory = questionService.getQuestionsBasedOnCategory(questionType);
        return questionsBasedOnCategory;
    }

    @PostMapping("add")
    public ResponseEntity<String> addNewQuestion(@RequestBody Questions question){
        return questionService.addQuestion(question);
    }

    @PutMapping("update/{questionId}")
    public ResponseEntity<String> updateQuestion(@PathVariable String questionId, @RequestBody Questions newQuestion){
        return questionService.updateQuestion(questionId, newQuestion);
    }

    @DeleteMapping("delete/{questionID}")
    public ResponseEntity<String> deleteQuestion(@PathVariable String questionID){
        return questionService.deleteQuestion(questionID);
    }
}
