package com.quizApp.quiz_service.service;

import com.quizApp.quiz_service.models.Questions;
import com.quizApp.quiz_service.models.Quiz;
import com.quizApp.quiz_service.models.Response;

import java.util.ArrayList;
import java.util.List;

public class QuizTestDataUtil {
    public QuizTestDataUtil() {
    }
    public static Questions createQuestion() {
        return Questions.builder()
                .id(1)
                .questionTitle("What is the output of the expression 2 ** 3 in Python?")
                .category("Python")
                .difficultyLevel("Easy")
                .option1("5")
                .option2("6")
                .option3("8")
                .option4("9")
                .rightAnswer("8")
                .build();
    }
    public static Questions createQuestion2() {
        return Questions.builder()
                .id(2)
                .questionTitle("Which keyword is used to define a function in Python?")
                .category("Python")
                .difficultyLevel("Easy")
                .option1("func")
                .option2("def")
                .option3("lambda")
                .option4("return")
                .rightAnswer("def")
                .build();
    }
    public static Questions createQuestion3() {
        return Questions.builder()
                .id(3)
                .questionTitle("Which of the following is the correct way to declare a variable in JavaScript?")
                .category("JavaScript")
                .option1("var")
                .option2("let")
                .option3("const")
                .option4("All Correct")
                .rightAnswer("All Correct")
                .build();
    }

    public static Quiz createQuiz() {
        return new Quiz().builder()
                .Id(1)
                .title("Quiz for Java")
                .questionIds(List.of(1,2,3))
                .build();
    }

    public static List<Response> getResponseList() {
        Response userResponse3 = new Response();
        userResponse3.setId(3);
        userResponse3.setUserResponse("All Correct");
        Response userResponse2 = new Response();
        userResponse2.setId(2);
        userResponse2.setUserResponse("def");
        Response userResponse1 = new Response();
        userResponse1.setId(1);
        userResponse1.setUserResponse("5");
        List<Response> userResponseList = new ArrayList<>();
        userResponseList.add(userResponse3);
        userResponseList.add(userResponse2);
        userResponseList.add(userResponse1);
        return userResponseList;
    }
}
