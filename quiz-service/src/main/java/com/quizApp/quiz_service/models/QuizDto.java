package com.quizApp.quiz_service.models;

import lombok.Data;

@Data
public class QuizDto {
    String category;
    Integer numberOfQuestions;
    String title;
}
