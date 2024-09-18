package com.quizApp.quiz_service.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String category;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Column(name = "question_title")
    private String questionTitle;

    @Column(name = "right_answer")
    private String rightAnswer;
}
