package com.qorvia.blogfeedbackservice.service;

import com.qorvia.blogfeedbackservice.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<?> addReview(ReviewDTO reviewDTO, Long userId, String userEmail);

    ResponseEntity<?> editReview(ReviewDTO reviewDTO, Long userId, String userEmail, String eventId);

    ResponseEntity<?> getAllReviews(String eventId);

    ResponseEntity<?> deleteReview(Long userId, String eventId);
}
