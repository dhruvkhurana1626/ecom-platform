package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.SellerRequest;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.model.Seller;
import com.example.demo.repository.SellerRepository;
import com.example.demo.transformers.SellerTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final Validation validation;

    public SellerResponse addSeller(SellerRequest sellerRequest) {

        // Validate seller details (email, phone, etc.)
        validation.validateNewSeller(sellerRequest);

        // Convert request DTO to Seller entity and persist
        Seller savedSeller = sellerRepository
                .save(SellerTransformer.sellerRequestToSeller(sellerRequest));

        // Convert saved entity to response DTO
        return SellerTransformer.sellerToSellerResponse(savedSeller);
    }

}
