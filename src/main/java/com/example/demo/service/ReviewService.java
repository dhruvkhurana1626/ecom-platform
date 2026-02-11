package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.transformers.ReviewTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final Validation validation;

    public List<ReviewResponse> getReviewById(int customerId) {

        // Validate if customer exists
        Customer customer = validation.checkIfCustomerExist(customerId);

        // Convert customer's reviews into response DTOs
        return customer.getReviewList()
                .stream()
                .map(ReviewTransformer::reviewToReviewResponse)
                .toList();
    }

    public ReviewResponse addReview(ReviewRequest reviewRequest,
                                    int customerId,
                                    int productId) {

        // Validate customer and product existence
        Customer customer = validation.checkIfCustomerExist(customerId);
        Product product = validation.checkIfProductExist(productId);

        // Convert request DTO to Review entity
        Review review = ReviewTransformer.reviewRequestToReview(reviewRequest);

        // Establish relationships
        review.setCustomer(customer);
        review.setProduct(product);

        // Persist review
        Review savedReview = reviewRepository.save(review);

        // Return response DTO
        return ReviewTransformer.reviewToReviewResponse(savedReview);
    }

    public List<ReviewResponse> getReviewByWord(String word) {

        // Fetch reviews containing the given keyword (case-insensitive)
        return reviewRepository.findByCommentContainingIgnoreCase(word)
                .stream()
                // Convert Review entity to response DTO
                .map(ReviewTransformer::reviewToReviewResponse)
                .toList();
    }
}
