package com.example.demo.service;

import com.example.demo.dto.request.ReviewRequest;
import com.example.demo.dto.response.ReviewResponse;
import com.example.demo.exception.CustomerNotFound;
import com.example.demo.exception.ProductNotFound;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.transformers.ReviewTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public List<Review> getReviewById(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFound("Customer Id is Invalid"));

        List<Review> reviewList = customer.getReviewList();
        return  reviewList;
    }

    public ReviewResponse addReview(ReviewRequest reviewRequest, int custId, int prodId) {
        Customer customer = customerRepository.findById(custId)
                .orElseThrow(()-> new CustomerNotFound("Customer id is Invalid"));

        Product product = productRepository.findById(prodId)
                .orElseThrow(()-> new ProductNotFound("Product id is Inavlid"));

        //Review Request to Review
        Review review = ReviewTransformer.reviewRequestToReview(reviewRequest);

        //Relationship
        review.setCustomer(customer);
        review.setProduct(product);

        //Saved
        Review savedReview = reviewRepository.save(review);

        return ReviewTransformer.reviewToReviewResponse(savedReview);
    }

    public List<ReviewResponse> getReviewByWord(String word) {

        //searching review with same word in repo
        List<Review> reviewList = reviewRepository.findByCommentContainingIgnoreCase(word);

        //converting review into review response
        List<ReviewResponse> reviewResponseList = new ArrayList<>();
        for(Review r : reviewList){
            reviewResponseList.add(ReviewTransformer.reviewToReviewResponse(r));
        }

        //returning final list
        return reviewResponseList;
    }
}
