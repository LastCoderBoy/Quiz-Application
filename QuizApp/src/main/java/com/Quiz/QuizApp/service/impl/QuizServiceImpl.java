package com.Quiz.QuizApp.service.impl;

import com.Quiz.QuizApp.models.QuestionWrapper;
import com.Quiz.QuizApp.models.Questions;
import com.Quiz.QuizApp.models.Quiz;
import com.Quiz.QuizApp.models.Response;
import com.Quiz.QuizApp.repository.QuestionRepository;
import com.Quiz.QuizApp.repository.QuizRepository;
import com.Quiz.QuizApp.service.QuizService;
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
    private final QuestionRepository questionRepository;
    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Override
    public ResponseEntity<String> createQuiz(String category, int numOfQuestions, String title) {
        try {
            List<Questions> byCategory = questionRepository.findQuestionsByCategory(category);
            if(!byCategory.isEmpty() && byCategory.size() >= numOfQuestions) {
                List<Questions> questionsByCategoryLimited = questionRepository.findQuestionsByCategoryLimited(category, numOfQuestions);
                Quiz quiz = new Quiz();
                quiz.setTitle(title);
                quiz.setQuestions(questionsByCategoryLimited);
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
            List<Questions> questionsFromDB = quizID.get().getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for(Questions question : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(question.getId(), question.getQuestionTitle(),
                        question.getOption1(), question.getOption2(),
                        question.getOption3(), question.getOption4());
                questionsForUser.add(qw);
            }
            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Integer> calculateResult(int id, List<Response> userResponses) {
        Quiz quiz = quizRepository.findById(id).get();
        List<Questions> questionsFromDB = quiz.getQuestions();
        int rightAnswers = 0;
        for (Response userResponse : userResponses) {
            for (Questions question : questionsFromDB) {
                if (userResponse.getId() == question.getId() &&
                        userResponse.getUserResponse().equals(question.getRightAnswer())) {
                    rightAnswers++;
                    break;
                }
            }
        }
        return new ResponseEntity<>(rightAnswers, HttpStatus.OK);
    }
}
