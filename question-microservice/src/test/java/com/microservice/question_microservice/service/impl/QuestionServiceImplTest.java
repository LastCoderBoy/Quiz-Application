package com.microservice.question_microservice.service.impl;

import com.microservice.question_microservice.QuestionTestDataUtil;
import com.microservice.question_microservice.models.QuestionWrapper;
import com.microservice.question_microservice.models.Questions;
import com.microservice.question_microservice.models.Response;
import com.microservice.question_microservice.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QuestionServiceImplTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl underTest;

    /**
     * Method under test: {@link QuestionServiceImpl}
     * Only checking if the Status Code is 200
     */
    @Test
    void testGetAllQuestions() {
        // Arrange: Mock the repository to return a list of questions
        List<Questions> questionsList = new ArrayList<>();
        questionsList.add(QuestionTestDataUtil.createQuestion());
        questionsList.add(QuestionTestDataUtil.createQuestion2());
        questionsList.add(QuestionTestDataUtil.createQuestion3());
        questionRepository.saveAll(questionsList);
        when(questionRepository.findAll()).thenReturn(questionsList);

        // Act: Call the method to test
        ResponseEntity<List<Questions>> actualAllQuestions = underTest.getAllQuestions();

        // Assert: Verify repository interaction and response details
        verify(questionRepository).findAll();  // Verify that findAll() was called once

        // Assert HTTP status
        HttpStatus statusCode = (HttpStatus) actualAllQuestions.getStatusCode();
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(200, actualAllQuestions.getStatusCodeValue());

        //Assert the response responseBody
        List<Questions> responseBody = actualAllQuestions.getBody();
        assertNotNull(responseBody);
        assertEquals(questionsList.size(), responseBody.size());
        assertSame(questionsList, responseBody);
        assertEquals(responseBody.get(1).getRightAnswer(),questionsList.get(1).getRightAnswer());
    }

    @Test
    void testGetQuestionsBasedOnCategory(){
        //Arrange: create the list of questions to be checked
        List<Questions> questionsList = new ArrayList<>();
        questionsList.add(QuestionTestDataUtil.createQuestion()); //Python
        questionsList.add(QuestionTestDataUtil.createQuestion2()); //Python
        questionsList.add(QuestionTestDataUtil.createQuestion3()); //JavaScript

        //Instead of returning the hard code return value, "thenAnswer()" method filters the list based on category.
        when(questionRepository.findQuestionsByCategory(anyString())).thenAnswer(invocation -> {
            String category = invocation.getArgument(0);
            return questionsList.stream()
                    .filter(q -> q.getCategory().equals(category))
                    .collect(Collectors.toList());
        });

        //Act:
        ResponseEntity<List<Questions>> ActualQuestionsBasedOnCategory = underTest.getQuestionsBasedOnCategory("Python");

        // Assert: Verify repository interaction and response details
        verify(questionRepository).findQuestionsByCategory("Python");

        //Assert the HTTP Status Code
        HttpStatus status = (HttpStatus) ActualQuestionsBasedOnCategory.getStatusCode();
        assertEquals(HttpStatus.OK, status);
        assertEquals(200, ActualQuestionsBasedOnCategory.getStatusCodeValue());

        //Assert the response responseBody
        List<Questions> responseBody = ActualQuestionsBasedOnCategory.getBody();
        assertNotNull(responseBody);
        assertEquals(2,responseBody.size());
        assertNotSame(questionsList, responseBody);
    }

    @Test
    void testAddQuestion(){
        //Arrange
        Questions question = QuestionTestDataUtil.createQuestion();
        when(questionRepository.save(question)).thenReturn(question);

        //Act
        ResponseEntity<String> responseFromUnderTest = underTest.addQuestion(question);

        //Assert the HTTPS status
        HttpStatus actualStatus = (HttpStatus) responseFromUnderTest.getStatusCode();
        assertEquals(HttpStatus.CREATED, actualStatus);
        assertEquals(201, responseFromUnderTest.getStatusCodeValue());
        //Assert the response
        String responseBody = responseFromUnderTest.getBody();
        assertNotNull(responseBody);
        assertEquals("Created Successfully!", responseBody);

    }

    @Test
    void testUpdateQuestion(){
        //Arrange
        List<Questions> questionsList = new ArrayList<>();
        Questions question1 = QuestionTestDataUtil.createQuestion();
        questionsList.add(question1); //ID: 1
        questionsList.add(QuestionTestDataUtil.createQuestion2()); //ID: 2
        questionsList.add(QuestionTestDataUtil.createQuestion3());  //ID: 3
        //When a mock repository for testing is used,
        //ensure that configuring the mock is important
        // in order to return the correct Optional<Questions> object when findById() is called with the given questionId.
        when(questionRepository.findById(1)).thenReturn(Optional.of(question1));

        //Act
        Questions newQuestion = new Questions();
        newQuestion.setQuestionTitle("Which collection type in Swift is unordered?");
        newQuestion.setCategory("Swift");
        newQuestion.setOption1("Array");
        newQuestion.setOption2("Set");
        newQuestion.setOption3("Dictionary");
        newQuestion.setOption4("Tuple");
        ResponseEntity<String> responseUnderTest = underTest.updateQuestion(1, newQuestion);

        verify(questionRepository).findById(eq(1));
        //Assert the HTTP status
        HttpStatus statusResponseFromUnderTest = (HttpStatus) responseUnderTest.getStatusCode();
        assertEquals(HttpStatus.OK, statusResponseFromUnderTest);
        assertEquals(200, responseUnderTest.getStatusCodeValue());

        //Assert the response from underTest
        assertEquals("Question 1 updated successfully!", responseUnderTest.getBody());

        assertEquals("Swift", question1.getCategory());
        assertEquals("Array", question1.getOption1());

    }

    @Test
    void testDeleteQuestion(){
        List<Questions> questionsList = new ArrayList<>();
        Questions question1 = QuestionTestDataUtil.createQuestion();
        questionsList.add(question1); //ID: 1
        questionsList.add(QuestionTestDataUtil.createQuestion2()); //ID: 2
        questionsList.add(QuestionTestDataUtil.createQuestion3());  //ID: 3

        when(questionRepository.findById(1)).thenReturn(Optional.of(question1));

        //Act
        ResponseEntity<String> respondeUnderTest = underTest.deleteQuestion(1);
        verify(questionRepository).findById(eq(1));

        //Assert the HTTP status
        HttpStatusCode statusCode = respondeUnderTest.getStatusCode();
        assertEquals(HttpStatus.OK,statusCode);
        assertEquals(200, respondeUnderTest.getStatusCodeValue());

        verify(questionRepository).deleteById(1);
        assertEquals("1 Deleted Successfully", respondeUnderTest.getBody());
    }

    @Test
    void testQuestionIdsForQuiz() {
        //Arrange
        List<Questions> questionsList = new ArrayList<>();
        Questions question1 = QuestionTestDataUtil.createQuestion();
        Questions question2 = QuestionTestDataUtil.createQuestion2();
        questionsList.add(question1); //ID: 1, Python
        questionsList.add(question2); //ID: 1, Python
        questionsList.add(QuestionTestDataUtil.createQuestion3());  //ID: 3, JavaScript

        List<Integer> integerList = new ArrayList<>();
        integerList.add(question1.getId());
        integerList.add(question2.getId());

        when(questionRepository.findQuestionsByCategory("Python")).thenReturn(questionsList);
        when(questionRepository.findQuestionsByCategoryLimited("Python", 2)).thenReturn(integerList);

        //Act
        ResponseEntity<List<Integer>> responseQuestionsForQuiz = underTest.getQuestionsForQuiz("Python", 2);
        verify(questionRepository).findQuestionsByCategoryLimited(eq("Python"), eq(2));

        HttpStatusCode statusCode = responseQuestionsForQuiz.getStatusCode();
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(200, responseQuestionsForQuiz.getStatusCodeValue());
        assertEquals(Objects.requireNonNull(responseQuestionsForQuiz.getBody()).size(), 2);
    }

    @Test
    void testGetQuestionsFromID(){
        //Arrange
        List<QuestionWrapper> questionsWrapperList = new ArrayList<>();
        QuestionWrapper question1 = new QuestionWrapper(
                1,
                "What is the output of the expression 2 ** 3 in Python?",
                "5",
                "6",
                "8",
                "9");
        QuestionWrapper question2 = new QuestionWrapper(
                2,
                "Which keyword is used to define a function in Python?",
                "func",
                "def",
                "lambda",
                "return"
        );
        questionsWrapperList.add(question1);
        questionsWrapperList.add(question2);

        when(questionRepository.getQuestionsFromID(List.of(1,2))).thenReturn(questionsWrapperList);

        //Act
        ResponseEntity<List<QuestionWrapper>> questionsFromIDList = underTest.getQuestionsFromID(List.of(1, 2));
        verify(questionRepository).getQuestionsFromID(eq(List.of(1,2)));

        //Assert
        assertEquals(questionsWrapperList, questionsFromIDList.getBody());
        assertEquals(questionsWrapperList.size(), questionsFromIDList.getBody().size());
        assertEquals(questionsWrapperList.get(0).getId(), questionsFromIDList.getBody().get(0).getId());
        assertEquals(questionsWrapperList.get(1).getId(), questionsFromIDList.getBody().get(1).getId());

        //Assert the Status Code
        HttpStatusCode statusCode = questionsFromIDList.getStatusCode();
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(200, questionsFromIDList.getStatusCodeValue());
    }

    @Test
    void testGetScore(){
        List<Questions> questionsList = new ArrayList<>();
        Questions question1 = QuestionTestDataUtil.createQuestion();
        Questions question2 = QuestionTestDataUtil.createQuestion2();//ID: 2; answer="def"
        questionsList.add(question1); //ID: 1; answer = "8"
        questionsList.add(question2);
        questionsList.add(QuestionTestDataUtil.createQuestion3());  //ID: 3

        List<Response> responseList = new ArrayList<>();
        Response response1 = new Response();
        response1.setId(1);
        response1.setUserResponse("8");
        Response response2 = new Response();
        response2.setId(2);
        response2.setUserResponse("def");
        responseList.add(response1);
        responseList.add(response2);

        when(questionRepository.findQuestionsById(response1.getId())).thenReturn(question1);
        when(questionRepository.findQuestionsById(response2.getId())).thenReturn(question2);

        //Act
        ResponseEntity<Integer> questionScoreList = underTest.getQuestionScore(responseList);
        //questionScoreList = 2
        verify(questionRepository).findQuestionsById(response1.getId());
        verify(questionRepository).findQuestionsById(response2.getId());

        assertEquals(2, questionScoreList.getBody().intValue());

        HttpStatusCode statusCode = questionScoreList.getStatusCode();
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(200, questionScoreList.getStatusCodeValue());

    }
}
