package com.quizApp.quiz_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.quizApp.quiz_service.feign.FromQuestionService;
import com.quizApp.quiz_service.models.Questions;
import com.quizApp.quiz_service.repository.QuizRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {QuizServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class QuizServiceImplDiffblueTest {
    @MockBean
    private FromQuestionService fromQuestionService;

    @MockBean
    private QuizRepository quizRepository;

    @Autowired
    private QuizServiceImpl quizServiceImpl;

    /**
     * Method under test: {@link QuizServiceImpl#createQuiz(String, int, String)}
     */
    @Test
    void testCreateQuiz() {
        // Arrange
        when(fromQuestionService.getQuestionsBasedOnCategory(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(200)));

        // Act
        ResponseEntity<String> actualCreateQuizResult = quizServiceImpl.createQuiz("Category", 10, "Dr");

        // Assert
        verify(fromQuestionService).getQuestionsBasedOnCategory(eq("Category"));
        HttpStatusCode statusCode = actualCreateQuizResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("An unexpected error occurred.", actualCreateQuizResult.getBody());
        assertEquals(500, actualCreateQuizResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualCreateQuizResult.hasBody());
        assertTrue(actualCreateQuizResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link QuizServiceImpl#createQuiz(String, int, String)}
     */
    @Test
    void testCreateQuiz2() {
        // Arrange
        when(fromQuestionService.getQuestionsBasedOnCategory(Mockito.<String>any())).thenReturn(null);

        // Act
        ResponseEntity<String> actualCreateQuizResult = quizServiceImpl.createQuiz("Category", 10, "Dr");

        // Assert
        verify(fromQuestionService).getQuestionsBasedOnCategory(eq("Category"));
        HttpStatusCode statusCode = actualCreateQuizResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("An unexpected error occurred.", actualCreateQuizResult.getBody());
        assertEquals(500, actualCreateQuizResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualCreateQuizResult.hasBody());
        assertTrue(actualCreateQuizResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link QuizServiceImpl#createQuiz(String, int, String)}
     */
    @Test
    void testCreateQuiz3() {
        // Arrange
        ArrayList<Questions> questionsList = new ArrayList<>();
        when(fromQuestionService.getQuestionsBasedOnCategory(Mockito.<String>any()))
                .thenReturn(new ResponseEntity<>(questionsList, HttpStatusCode.valueOf(200)));

        // Act
        ResponseEntity<String> actualCreateQuizResult = quizServiceImpl.createQuiz("Category", 10, "Dr");

        // Assert
        verify(fromQuestionService).getQuestionsBasedOnCategory(eq("Category"));
        HttpStatusCode statusCode = actualCreateQuizResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("No questions found for the specified category.\n"
                + "Or Couldn't able to generate 10 quiz(es) for this category", actualCreateQuizResult.getBody());
        assertEquals(400, actualCreateQuizResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualCreateQuizResult.hasBody());
        assertTrue(actualCreateQuizResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link QuizServiceImpl#createQuiz(String, int, String)}
     */
    @Test
    void testCreateQuiz4() {
        // Arrange
        ResponseEntity<List<Questions>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenThrow(new EmptyResultDataAccessException(3));
        when(fromQuestionService.getQuestionsBasedOnCategory(Mockito.<String>any())).thenReturn(responseEntity);

        // Act
        ResponseEntity<String> actualCreateQuizResult = quizServiceImpl.createQuiz("Category", 10, "Dr");

        // Assert
        verify(fromQuestionService).getQuestionsBasedOnCategory(eq("Category"));
        verify(responseEntity).getBody();
        HttpStatusCode statusCode = actualCreateQuizResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Error accessing the database.", actualCreateQuizResult.getBody());
        assertEquals(500, actualCreateQuizResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualCreateQuizResult.hasBody());
        assertTrue(actualCreateQuizResult.getHeaders().isEmpty());
    }
}
