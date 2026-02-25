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

}
