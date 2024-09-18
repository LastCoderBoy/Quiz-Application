package com.quizApp.quiz_service.service.impl;

import com.quizApp.quiz_service.feign.FromQuestionService;
import com.quizApp.quiz_service.models.QuestionWrapper;
import com.quizApp.quiz_service.models.Questions;
import com.quizApp.quiz_service.models.Quiz;
import com.quizApp.quiz_service.models.Response;
import com.quizApp.quiz_service.repository.QuizRepository;
import com.quizApp.quiz_service.service.QuizService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);
    private final FromQuestionService fromQuestionService;

    @Override
    public ResponseEntity<String> createQuiz(String category, int numOfQuestions, String title) {
        try {
            List<Questions> questionsByCategory = fromQuestionService.getQuestionsBasedOnCategory(category).getBody();
            if(!questionsByCategory.isEmpty() && questionsByCategory.size() >= numOfQuestions) {
                List<Integer> questionsForQuiz = fromQuestionService.getQuestionsForQuiz(category, numOfQuestions).getBody();
                Quiz quiz = new Quiz();
                quiz.setTitle(title);
                quiz.setQuestionIds(questionsForQuiz);
                quizRepository.save(quiz);
                return new ResponseEntity<>("Success!", HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>("No questions found for the specified category.\nOr Couldn't able to generate " +
                        numOfQuestions + " quiz for this category", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException dae) {
            // Handle specific database exceptions
            logger.error("Database error while creating Quiz for category: " + category, dae);
            return new ResponseEntity<>("Error accessing the database.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Handle general exceptions
            logger.error("Unexpected error creating Quiz for category: " + category, e);
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestionsByID(int id) {
        Optional<Quiz> quizID = quizRepository.findById(id);
        if(quizID.isPresent()) {
            List<Integer> questionsFromDB = quizID.get().getQuestionIds();
            List<QuestionWrapper> quizQuestions = fromQuestionService.getQuestionsFromID(questionsFromDB).getBody();
            return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Integer> calculateResult(int id, List<Response> userResponses) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if(quiz.isPresent()) {
            ResponseEntity<Integer> quizScore = fromQuestionService.getQuestionScore(userResponses);
            return quizScore;
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
