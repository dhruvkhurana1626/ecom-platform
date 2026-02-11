package com.example.demo.controller;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.model.Review;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping
    public ResponseEntity getReviewById(@RequestParam("id") int id) {
        try {
            List<ReviewResponse> response = reviewService.getReviewById(id);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (CustomerNotFound e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity addReview(@RequestBody ReviewRequest reviewRequest,
                                    @RequestParam ("cid") int custId,
                                    @RequestParam ("pid") int prodId){
        try {
            ReviewResponse reviewResponse = reviewService.addReview(reviewRequest,custId,prodId);
            return new ResponseEntity(reviewResponse,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/review-by-word")
    public ResponseEntity getReviewByWord(@RequestParam String word){
        List<ReviewResponse> reviewResponse = reviewService.getReviewByWord(word);
        return new ResponseEntity(reviewResponse,HttpStatus.OK);
    }
}
