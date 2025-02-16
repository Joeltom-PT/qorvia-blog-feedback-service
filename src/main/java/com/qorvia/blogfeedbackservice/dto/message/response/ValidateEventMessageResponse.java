package com.qorvia.blogfeedbackservice.dto.message.response;

import lombok.Data;

@Data
public class ValidateEventMessageResponse {
    private Boolean isValid;
    private String message;
}