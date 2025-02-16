package com.qorvia.blogfeedbackservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReviewDTO {
    private Long id;

    private String content;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Long userId;
    private String userEmail;
    private String userName;

    private String eventId;

    private Boolean isVerified;
}
