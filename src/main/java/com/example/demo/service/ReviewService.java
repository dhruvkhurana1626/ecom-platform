package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.transformers.ReviewTransformer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final Validation validation;

    public List<ReviewResponse> getReviewByCustomer() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        List<Review> reviews = reviewRepository.findByCustomerId(customer.getId());

        return reviews.stream()
                .map(ReviewTransformer::reviewToReviewResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse addReview(ReviewRequest reviewRequest,
                                    Integer productId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        Product product = validation.checkProductByProductId_ReturnProduct(productId);

        // Prevent duplicate review
        if (reviewRepository.existsByCustomerIdAndProductId(
                customer.getId(), product.getId())) {
            throw new InvalidRequestException("You have already reviewed this product");
        }

        Review review = ReviewTransformer.reviewRequestToReview(reviewRequest);

        review.setCustomer(customer);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);
        return ReviewTransformer.reviewToReviewResponse(savedReview);
    }

    public List<ReviewResponse> getReviewByWord(String word) {

        if (word == null || word.trim().isEmpty()) {
            throw new InvalidRequestException("Search word cannot be empty");
        }

        return reviewRepository
                .findByCommentContainingIgnoreCase(word.trim())
                .stream()
                .map(ReviewTransformer::reviewToReviewResponse)
                .toList();
    }

    public List<ReviewResponse> getReviewByProductId(Integer productId) {

        validation.checkProductExistById(productId);

        List<Review> reviewList = (List<Review>) reviewRepository.findByProductId(productId);

        return reviewList
                .stream()
                .map(ReviewTransformer::reviewToReviewResponse)
                .toList();
    }

    @Transactional
    public void deleteReview(Integer reviewId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Ownership check
        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidRequestException("You cannot delete this review");
        }

        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewResponse updateReview(Integer reviewId,
                                       ReviewRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        // Ownership check
        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidRequestException("You cannot update this review");
        }

        review.setComment(request.getComment());
        review.setRating(request.getRating());

        return ReviewTransformer.reviewToReviewResponse(review);
    }
}
