package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {

        return ResponseEntity.ok(reviewService.getReviewByCustomer());
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody @Valid ReviewRequest reviewRequest,
            @PathVariable Integer productId) {

        return new ResponseEntity<>(
                reviewService.addReview(reviewRequest, productId),
                HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReviewResponse>> getReviewByWord(
            @RequestParam String word) {

        return ResponseEntity.ok(reviewService.getReviewByWord(word));
    }

    @GetMapping("/search/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewByProductId(
            @PathVariable Integer productId){

        return ResponseEntity.ok(reviewService.getReviewByProductId(productId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Integer reviewId) {

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Integer reviewId,
            @RequestBody @Valid ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, request)
        );
    }
}