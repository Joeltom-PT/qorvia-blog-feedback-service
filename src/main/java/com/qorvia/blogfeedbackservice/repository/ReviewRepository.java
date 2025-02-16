package com.qorvia.blogfeedbackservice.repository;

import com.qorvia.blogfeedbackservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEventId(String eventId);

    Optional<Review> findByEventIdAndUserEmail(String eventId, String userEmail);

    Optional<Review> findByEventIdAndUserId(String eventId, Long userId);
}
