package com.example.demo.transformers;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.model.Review;

public class ReviewTransformer {

    public static Review reviewRequestToReview(ReviewRequest reviewRequest){
        Review review = Review.builder()
                .comment(reviewRequest.getComment())
                .rating(reviewRequest.getRating())
                .build();

        return review;
    }

    public static ReviewResponse reviewToReviewResponse(Review review){
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .comment(review.getComment())
                .rating(review.getRating())
                .productName(review.getProduct().getName())
                .productPrice(review.getProduct().getPrice())
                .build();

        return reviewResponse;
    }
}
