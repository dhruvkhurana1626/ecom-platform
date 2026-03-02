package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.service.ReviewService;
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

    @GetMapping
    public ResponseEntity getReviewById(@RequestParam("id") int id) {

        List<ReviewResponse> response =
                reviewService.getReviewById(id);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addReview(@RequestBody ReviewRequest reviewRequest,
                                    @RequestParam("cid") int custId,
                                    @RequestParam("pid") int prodId) {

        ReviewResponse reviewResponse =
                reviewService.addReview(reviewRequest, custId, prodId);

        return new ResponseEntity(reviewResponse, HttpStatus.CREATED);
    }


    @GetMapping("/review-by-word")
    public ResponseEntity getReviewByWord(@RequestParam String word) {

        List<ReviewResponse> reviewResponse =
                reviewService.getReviewByWord(word);

        return new ResponseEntity(reviewResponse, HttpStatus.OK);
    }
}