package com.microservice.question_microservice.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response {
    private Integer Id;
    private String userResponse;
}
