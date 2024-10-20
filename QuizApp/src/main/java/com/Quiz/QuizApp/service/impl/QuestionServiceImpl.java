package com.Quiz.QuizApp.service.impl;

import com.Quiz.QuizApp.exceptionHandler.ResourceNotFoundException;
import com.Quiz.QuizApp.models.Questions;
import com.Quiz.QuizApp.repository.QuestionRepository;
import com.Quiz.QuizApp.service.QuestionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    private final QuestionRepository questionRepository;

    @Override
    public ResponseEntity<List<Questions>> getAllQuestions() {
        try {
            List<Questions> questionsList = questionRepository.findAll();
            return new ResponseEntity<>(questionsList, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            logger.error("No questions found", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        } catch (DataAccessException e) {
            logger.error("Database access error occurred while fetching questions", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching questions", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<List<Questions>> getQuestionsBasedOnCategory(String categoryType) {
        try {
            List<Questions> questionsByCategory = questionRepository.findQuestionsByCategory(categoryType);
            return new ResponseEntity<>(questionsByCategory, HttpStatus.OK);
        }catch (Exception e){
            logger.error("An unexpected error occurred while fetching questions based on category", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Questions> getQuestionByID(String questionID) {
        try {
            Optional<Questions> questionOpt = questionRepository.findById(Integer.valueOf(questionID));
            return questionOpt.map(questions -> new ResponseEntity<>(questions, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (NumberFormatException e) {
            logger.error("Invalid question ID format: " + questionID, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while loading question with ID: " + questionID, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addQuestion(Questions question) {
        try {
            questionRepository.save(question);
            return new ResponseEntity<>("Created Successfully!",HttpStatus.CREATED);
        }catch (HttpClientErrorException.BadRequest bd){
            return new ResponseEntity<>("Wrong input format!", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateQuestion(String questionId, Questions newQuestion) {
        try {
            Optional<Questions> oldQuestion = questionRepository.findById(Integer.valueOf(questionId));
            if (oldQuestion.isPresent()) {
                Questions updatedQuestion = oldQuestion.get();
                updatedQuestion.setCategory(newQuestion.getCategory());
                updatedQuestion.setQuestionTitle(newQuestion.getQuestionTitle());
                updatedQuestion.setDifficultyLevel(newQuestion.getDifficultyLevel());
                updatedQuestion.setOption1(newQuestion.getOption1());
                updatedQuestion.setOption2(newQuestion.getOption2());
                updatedQuestion.setOption3(newQuestion.getOption3());
                updatedQuestion.setOption4(newQuestion.getOption4());
                updatedQuestion.setQuestionTitle(newQuestion.getQuestionTitle());
                updatedQuestion.setRightAnswer(newQuestion.getRightAnswer());
                questionRepository.save(updatedQuestion);
                return new ResponseEntity<>("Question " + questionId + " updated successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question not found with ID: " + questionId, HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid question ID format: " + questionId, e);
            return new ResponseEntity<>("Invalid question ID format: " + questionId, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Error while updating question " + questionId + ": ", e);
            return new ResponseEntity<>("An error occurred while updating the question.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteQuestion(String questionID) {
        try {
            if(questionRepository.findById(Integer.valueOf(questionID)).isPresent()) {
                questionRepository.deleteById(Integer.valueOf(questionID));
                return new ResponseEntity<>(questionID + " Deleted Successfully", HttpStatus.OK);
            }else {
                return new ResponseEntity<>(questionID + " ID is not present!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting question with ID: " + questionID, e);
            return new ResponseEntity<>("Question not found with ID: " + questionID, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
