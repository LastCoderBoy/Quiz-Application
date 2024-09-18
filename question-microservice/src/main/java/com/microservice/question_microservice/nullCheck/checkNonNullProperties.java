package com.microservice.question_microservice.nullCheck;

import java.lang.reflect.Field;

public class checkNonNullProperties {
    public static void copyProperties(Object source, Object target){
        for(Field field : source.getClass().getDeclaredFields()){
            try{
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
