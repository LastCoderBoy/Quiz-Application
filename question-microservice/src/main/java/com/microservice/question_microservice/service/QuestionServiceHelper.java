package com.microservice.question_microservice.service;

import com.microservice.question_microservice.models.Questions;
import java.lang.reflect.Field;

public class QuestionServiceHelper {
    public static boolean areFieldsValid(Questions question) {
        return question.getQuestionTitle() != null && !question.getQuestionTitle().isEmpty() &&
                question.getCategory() != null && !question.getCategory().isEmpty() &&
                question.getOption1() != null && !question.getOption1().isEmpty() &&
                question.getOption2() != null && !question.getOption2().isEmpty() &&
                question.getOption3() != null && !question.getOption3().isEmpty() &&
                question.getOption4() != null && !question.getOption4().isEmpty() &&
                question.getDifficultyLevel() != null && !question.getDifficultyLevel().isEmpty();
    }
    public static void copyProperties(Object source, Object target){
        for(Field field : source.getClass().getDeclaredFields()){
            try{
                //Allows to access the private fields
                field.setAccessible(true);
                Object value = field.get(source);
                if(value != null){
                    field.set(target, value);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
