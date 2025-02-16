package com.qorvia.blogfeedbackservice.service;

import com.qorvia.blogfeedbackservice.clients.EventManagementClient;
import com.qorvia.blogfeedbackservice.dto.ReviewDTO;
import com.qorvia.blogfeedbackservice.model.Review;
import com.qorvia.blogfeedbackservice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EventManagementClient eventManagementClient;

    @Override
    public ResponseEntity<?> addReview(ReviewDTO reviewDTO, Long userId, String userEmail) {
        try {

            boolean isEventValid = eventManagementClient.validateEvent(reviewDTO.getEventId());

            if (isEventValid) {
                Review review = new Review();
                review.setContent(reviewDTO.getContent());
                review.setCreatedAt(LocalDate.now());
                review.setUpdatedAt(LocalDate.now());
                review.setUserName(reviewDTO.getUserName());
                review.setUserEmail(userEmail);
                review.setUserId(userId);
                review.setEventId(reviewDTO.getEventId());
                review.setIsVerified(true);

                reviewRepository.save(review);
                log.info("Review successfully saved as verified.");
                return ResponseEntity.ok("Review added and verified successfully.");
            } else {
                log.warn("Invalid event. Review not saved.");
                return ResponseEntity.badRequest().body("Invalid event. Review could not be verified.");
            }

        } catch (Exception e) {
            log.error("Error occurred while adding review: ", e);
            return ResponseEntity.status(500).body("An error occurred while processing the review.");
        }
    }

    @Override
    public ResponseEntity<?> editReview(ReviewDTO reviewDTO, Long userId, String userEmail, String eventId) {
        log.info("Trying to edit review by user : {} to eventId : {}", userId, eventId);
        try {
            Optional<Review> existingReviewOpt = reviewRepository.findByEventIdAndUserEmail(eventId, userEmail);

            if (existingReviewOpt.isEmpty()) {
                log.warn("No review found for eventId: {} and userEmail: {}", reviewDTO.getEventId(), userEmail);
                return ResponseEntity.badRequest().body("Review not found for the specified event and user.");
            }

            Review existingReview = existingReviewOpt.get();

            if (!existingReview.getUserEmail().equals(userEmail)) {
                log.warn("Unauthorized attempt to edit review by userEmail: {}", userEmail);
                return ResponseEntity.status(403).body("You are not authorized to edit this review.");
            }

            existingReview.setContent(reviewDTO.getContent());
            existingReview.setUpdatedAt(LocalDate.now());

            reviewRepository.save(existingReview);

            log.info("Review successfully updated for eventId: {}", reviewDTO.getEventId());
            return ResponseEntity.ok("Review updated successfully.");

        } catch (Exception e) {
            log.error("Error occurred while editing review: ", e);
            return ResponseEntity.status(500).body("An error occurred while updating the review.");
        }
    }


    @Override
    public ResponseEntity<?> getAllReviews(String eventId) {
        try {
            List<Review> reviews = reviewRepository.findByEventId(eventId);

            if (reviews.isEmpty()) {
                log.info("No reviews found for eventId: {}", eventId);
                return ResponseEntity.ok("No reviews found for this event.");
            }

            List<ReviewDTO> reviewDTOs = reviews.stream().map(review ->
                    ReviewDTO.builder()
                            .id(review.getId())
                            .content(review.getContent())
                            .userName(review.getUserName())
                            .userEmail(review.getUserEmail())
                            .eventId(review.getEventId())
                            .createdAt(review.getCreatedAt())
                            .updatedAt(review.getUpdatedAt())
                            .build()
            ).toList();

            log.info("Successfully fetched {} reviews for eventId: {}", reviews.size(), eventId);
            return ResponseEntity.ok(reviewDTOs);

        } catch (Exception e) {
            log.error("Error occurred while fetching reviews: ", e);
            return ResponseEntity.status(500).body("An error occurred while retrieving reviews.");
        }
    }

    @Override
    public ResponseEntity<?> deleteReview(Long userId, String eventId) {
        log.info("Trying to delete review by userId: {} for eventId: {}", userId, eventId);
        try {
            Optional<Review> existingReviewOpt = reviewRepository.findByEventIdAndUserId(eventId, userId);
            if (existingReviewOpt.isEmpty()) {
                log.warn("No review found for eventId: {} and userId: {}", eventId, userId);
                return ResponseEntity.badRequest().body("Review not found for the specified event and user.");
            }

            Review existingReview = existingReviewOpt.get();

            if (!existingReview.getUserId().equals(userId)) {
                log.warn("Unauthorized attempt to delete review by userId: {}", userId);
                return ResponseEntity.status(403).body("You are not authorized to delete this review.");
            }

            reviewRepository.delete(existingReview);
            log.info("Review successfully deleted for eventId: {}", eventId);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (Exception e) {
            log.error("Error occurred while deleting review: ", e);
            return ResponseEntity.status(500).body("An error occurred while deleting the review.");
        }
    }

}