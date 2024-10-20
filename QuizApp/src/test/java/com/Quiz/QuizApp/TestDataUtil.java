package com.Quiz.QuizApp;

import com.Quiz.QuizApp.models.Questions;

public class TestDataUtil {
    public static Questions questionOne(){
        Questions question1 = new Questions();
        question1.setCategory("Java");
        question1.setDifficultyLevel("Medium");
        question1.setOption1(String.valueOf(2));
        question1.setOption2(String.valueOf(4));
        question1.setOption3(String.valueOf(8));
        question1.setOption4(String.valueOf(16));
        question1.setQuestionTitle("What is the size of an int in Java?");
        question1.setRightAnswer(String.valueOf(4));

        return question1;
    }

    public static Questions questionTwo(){
        Questions question2 = new Questions();
        question2.setCategory("Java");
        question2.setDifficultyLevel("Easy");
        question2.setOption1("Object-oriented");
        question2.setOption2("Use of pointers");
        question2.setOption3("Platform-independent");
        question2.setOption4("'Multithreading'");
        question2.setQuestionTitle("Which of the following is not a Java feature?");
        question2.setRightAnswer("Use of pointers");

        return question2;
    }
    public static Questions questionThree(){
        Questions question3 = new Questions();
        question3.setCategory("Java");
        question3.setDifficultyLevel("Easy");
        question3.setOption1("'implements'");
        question3.setOption2("'extends'");
        question3.setOption3("import");
        question3.setOption4("inherits");
        question3.setQuestionTitle("Which keyword is used to inherit a class in Java?");
        question3.setRightAnswer("extends");

        return question3;
    }
}
