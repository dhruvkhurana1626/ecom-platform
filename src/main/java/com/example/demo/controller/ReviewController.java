package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    /**
     * ReviewService encapsulates review validation,
     * customer/product verification and persistence logic.
     * Controller remains a thin delegation layer.
     */
    private final ReviewService reviewService;

    /**
     * Retrieves reviews by ID.
     * Any domain exception (e.g., CustomerNotFound)
     * is propagated to GlobalExceptionHandler.
     */
    @GetMapping
    public ResponseEntity getReviewById(@RequestParam("id") int id) {

        List<ReviewResponse> response =
                reviewService.getReviewById(id);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * Adds a new review for a given customer and product.
     *
     * - Validates customer existence
     * - Validates product existence
     * - Persists review entity
     *
     * Exception handling is centralized via @RestControllerAdvice.
     */
    @PostMapping
    public ResponseEntity addReview(@RequestBody ReviewRequest reviewRequest,
                                    @RequestParam("cid") int custId,
                                    @RequestParam("pid") int prodId) {

        ReviewResponse reviewResponse =
                reviewService.addReview(reviewRequest, custId, prodId);

        return new ResponseEntity(reviewResponse, HttpStatus.CREATED);
    }

    /**
     * Retrieves reviews containing a specific keyword.
     * Filtering logic is handled inside the service layer.
     */
    @GetMapping("/review-by-word")
    public ResponseEntity getReviewByWord(@RequestParam String word) {

        List<ReviewResponse> reviewResponse =
                reviewService.getReviewByWord(word);

        return new ResponseEntity(reviewResponse, HttpStatus.OK);
    }
}