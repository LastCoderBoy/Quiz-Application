package com.microservice.question_microservice.controller;

import com.microservice.question_microservice.models.QuestionWrapper;
import com.microservice.question_microservice.models.Questions;
import com.microservice.question_microservice.models.Response;
import com.microservice.question_microservice.service.impl.QuestionServiceImpl;
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
    public ResponseEntity<List<Questions>> getQuestionsBasedOnCategory(@PathVariable String questionType){
        return questionService.getQuestionsBasedOnCategory(questionType);
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

    //generate
    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam Integer numOfQuestions){
        return questionService.getQuestionsForQuiz(category, numOfQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromID(@RequestBody List<Integer> questionIDs){
        return questionService.getQuestionsFromID(questionIDs);
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getQuestionScore(@RequestBody List<Response> responses){
        return questionService.getQuestionScore(responses);
    }
}
