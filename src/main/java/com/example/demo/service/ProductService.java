package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.transformers.ProductTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final Validation validation;

    @Transactional
    public ProductResponse addProduct(ProductRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Seller seller = validation.checkSellerByEmail_ReturnSeller(email);

        Product product = ProductTransformer.productRequestToProduct(request);
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);

        return ProductTransformer.productToProductResponse(savedProduct);
    }

    public List<ProductResponse> getProductByCategory(Category category) {

        // Fetch products by category
        return productRepository.findByCategory(category)
                .stream()
                .map(ProductTransformer::productToProductResponse)
                .toList();
    }
}
