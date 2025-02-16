package com.qorvia.blogfeedbackservice.controller;

import com.qorvia.blogfeedbackservice.dto.ReviewDTO;
import com.qorvia.blogfeedbackservice.service.ReviewService;
import com.qorvia.blogfeedbackservice.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO reviewDTO, HttpServletRequest servletRequest){
        String token = jwtService.getJWTFromRequest(servletRequest);
        Long userId = jwtService.getUserIdFromToken(token);
        String userEmail = jwtService.getEmailFromToken(token);
        return reviewService.addReview(reviewDTO, userId, userEmail);
    }

    @PutMapping("/edit/{eventId}")
    public ResponseEntity<?> editReview(@PathVariable("eventId") String eventId, @RequestBody ReviewDTO reviewDTO, HttpServletRequest servletRequest){
        String token = jwtService.getJWTFromRequest(servletRequest);
        Long userId = jwtService.getUserIdFromToken(token);
        String userEmail = jwtService.getEmailFromToken(token);
        return reviewService.editReview(reviewDTO, userId, userEmail, eventId);
    }


    @GetMapping("/get-reviews/{eventId}")
    public ResponseEntity<?> getAllReviewsByEventId(@PathVariable("eventId") String eventId){
        return reviewService.getAllReviews(eventId);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteReview(@PathVariable("eventId") String eventId, HttpServletRequest servletRequest){
        String token = jwtService.getJWTFromRequest(servletRequest);
        Long userId = jwtService.getUserIdFromToken(token);
        String userEmail = jwtService.getEmailFromToken(token);
        return reviewService.deleteReview(userId, eventId);
    }



}
