package com.quizApp.quiz_service.service.impl;

import com.quizApp.quiz_service.feign.FromQuestionService;
import com.quizApp.quiz_service.models.QuestionWrapper;
import com.quizApp.quiz_service.models.Questions;
import com.quizApp.quiz_service.models.Quiz;
import com.quizApp.quiz_service.models.Response;
import com.quizApp.quiz_service.repository.QuizRepository;
import com.quizApp.quiz_service.service.QuizTestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QuizServiceImplTests {
    @Mock
    private QuizRepository quizRepository;

    @Mock
    private FromQuestionService fromQuestionService;

    @InjectMocks
    private QuizServiceImpl underTest;

    /**
     * Method under test: {@link QuizServiceImpl}
     * Only checking if the Status Code is 200
     */

    @Test
    void testCreateQuiz(){
        //Arrange
        List<Questions> questionsList = new ArrayList<>();
        Questions question1 = QuizTestDataUtil.createQuestion(); //Python
        Questions question2 = QuizTestDataUtil.createQuestion2(); //Python
        questionsList.add(question1);
        questionsList.add(question2);
        List<Integer> questionIds = new ArrayList<>();
        questionIds.add(question1.getId());
        questionIds.add(question2.getId());

        Mockito.when(fromQuestionService.getQuestionsBasedOnCategory("Python"))
                .thenReturn(new ResponseEntity<>(questionsList, HttpStatus.OK));
        Mockito.when(fromQuestionService.getQuestionsForQuiz("Python", 2))
                .thenReturn(new ResponseEntity<>(questionIds, HttpStatus.OK));

        //Act
        ResponseEntity<String> underTestResult = underTest.createQuiz("Python", 2, "Python Quiz");

        //Assertions
        Mockito.verify(fromQuestionService).getQuestionsBasedOnCategory("Python");
        Mockito.verify(fromQuestionService).getQuestionsForQuiz("Python", 2);

        HttpStatusCode httpStatusCode = underTestResult.getStatusCode();
        assertEquals(201,httpStatusCode.value());
        assertEquals(CREATED, httpStatusCode);
        assertEquals("Success!", underTestResult.getBody());

    }

    @Test
    void testGetQuizQuestionByIds(){
        Quiz quiz = QuizTestDataUtil.createQuiz();
        List<QuestionWrapper> questionWrapperList = new ArrayList<>();
        QuestionWrapper questionWrapper2 = new QuestionWrapper(2,
                "Which method must be implemented by all threads in Java?",
                "start()",
                "run()",
                "stop()",
                "main()");
        QuestionWrapper questionWrapper1 = new QuestionWrapper(1,
                "Which method is used to get the length of an array in Java?",
                "size()",
                "length",
                "getLength()",
                "arraySize()");
        questionWrapperList.add(questionWrapper1);
        questionWrapperList.add(questionWrapper2);

        Mockito.when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        Mockito.when(fromQuestionService.getQuestionsFromID(quiz.getQuestionIds())).thenReturn(new ResponseEntity<>(questionWrapperList, OK));

        //Act
        ResponseEntity<List<QuestionWrapper>> quizQuestionsByID = underTest.getQuizQuestionsByID(1);

        //Assert
        Mockito.verify(quizRepository).findById(1);
        Mockito.verify(fromQuestionService).getQuestionsFromID(quiz.getQuestionIds());

        HttpStatusCode statusCode = quizQuestionsByID.getStatusCode();
        assertEquals(200,statusCode.value());
        assertEquals(OK, statusCode);
        assertEquals(questionWrapperList, quizQuestionsByID.getBody());
    }

    @Test
    void testCalculateResult(){
        Questions questions = QuizTestDataUtil.createQuestion();
        Questions questions2 = QuizTestDataUtil.createQuestion2();
        Questions questions3 = QuizTestDataUtil.createQuestion3();
        List<Questions> questionsList = new ArrayList<>();
        questionsList.add(questions);
        questionsList.add(questions2);
        questionsList.add(questions3);
        Quiz quiz = QuizTestDataUtil.createQuiz();

        List<Response> userResponseList = QuizTestDataUtil.getResponseList();

        Mockito.when(quizRepository.findById(1)).thenReturn(Optional.of(quiz));
        Mockito.when(fromQuestionService.getQuestionScore(userResponseList)).thenReturn(new ResponseEntity<>(2, OK));

        //Act
        ResponseEntity<Integer> userResult = underTest.calculateResult(1, userResponseList);

        Mockito.verify(quizRepository).findById(1);
        Mockito.verify(fromQuestionService).getQuestionScore(userResponseList);
        assertTrue(quiz.getQuestionIds().size() >= userResponseList.size());
        assertEquals(2, userResult.getBody());
        HttpStatusCode statusCode = userResult.getStatusCode();
        assertEquals(200,statusCode.value());
        assertEquals(OK, statusCode);
    }

}
