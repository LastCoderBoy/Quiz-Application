package com.microservice.question_microservice;

import com.microservice.question_microservice.models.Questions;

public final class QuestionTestDataUtil {
    public QuestionTestDataUtil() {
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
}
