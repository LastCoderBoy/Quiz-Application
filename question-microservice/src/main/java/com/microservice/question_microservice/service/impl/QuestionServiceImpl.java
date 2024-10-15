package com.microservice.question_microservice.service.impl;

import com.microservice.question_microservice.models.QuestionWrapper;
import com.microservice.question_microservice.models.Questions;
import com.microservice.question_microservice.models.Response;
import com.microservice.question_microservice.repository.QuestionRepository;
import com.microservice.question_microservice.service.QuestionService;
import com.microservice.question_microservice.service.QuestionServiceHelper;
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
//    @Override
//    public ResponseEntity<Questions> getQuestionByID(String questionID) {
//        try {
//            Optional<Questions> questionOpt = questionRepository.findById(Integer.valueOf(questionID));
//            return questionOpt.map(questions -> new ResponseEntity<>(questions, HttpStatus.OK))
//                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (NumberFormatException e) {
//            logger.error("Invalid question ID format: " + questionID, e);
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            logger.error("An unexpected error occurred while loading question with ID: " + questionID, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @Override
    public ResponseEntity<String> addQuestion(Questions question) {
        try {
            //New question cannot be added unless all the columns are filled
            if(QuestionServiceHelper.areFieldsValid(question)) {
                questionRepository.save(question);
                return new ResponseEntity<>("Created Successfully!",HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Not All columns are filled", HttpStatus.BAD_REQUEST);
        }catch (HttpClientErrorException.BadRequest bd){
            return new ResponseEntity<>("Wrong input format!", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateQuestion(Integer questionId, Questions newQuestion) {
        try {
            Optional<Questions> oldQuestion = questionRepository.findById(questionId);
            if (oldQuestion.isPresent()) {
                Questions questionToBeUpdated = oldQuestion.get();
                // If the user only enters a value for single column,
                // it will update only the selected column and leaves the rest as it was.
                QuestionServiceHelper.copyProperties(newQuestion, questionToBeUpdated);
                questionRepository.save(questionToBeUpdated);
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
    public ResponseEntity<String> deleteQuestion(Integer questionID) {
        try {
            if(questionRepository.findById(questionID).isPresent()) {
                questionRepository.deleteById(questionID);
                return new ResponseEntity<>(questionID + " Deleted Successfully", HttpStatus.OK);
            }else {
                return new ResponseEntity<>(questionID + " ID is not present!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting question with ID: " + questionID, e);
            return new ResponseEntity<>("Question not found with ID: " + questionID, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numOfQuestions) {
        List<Questions> byCategory = questionRepository.findQuestionsByCategory(category);
        if (!byCategory.isEmpty() && byCategory.size() >= numOfQuestions) {
            List<Integer> questionIdsForQuiz = questionRepository.findQuestionsByCategoryLimited(category, numOfQuestions);
            return new ResponseEntity<>(questionIdsForQuiz, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromID(List<Integer> questionIDs) {
        List<QuestionWrapper> wrappers = questionRepository.getQuestionsFromID(questionIDs);
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getQuestionScore(List<Response> responses) {
        int rightAnswers = 0;
        for (Response userResponse : responses) {
            Questions questionsById = questionRepository.findQuestionsById(userResponse.getId());
                if (userResponse.getUserResponse().equals(questionsById.getRightAnswer())) {
                    rightAnswers++;
                }
            }
        return new ResponseEntity<>(rightAnswers, HttpStatus.OK);
    }
}
