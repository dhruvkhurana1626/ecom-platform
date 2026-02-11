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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final Validation validation;

    @Transactional
    public ProductResponse addProduct(ProductRequest productRequest, int sellerId) {

        // Validate if seller exists
        Seller seller = validation.checkIfSellerExist(sellerId);

        // Convert request DTO to Product entity
        Product product = ProductTransformer.productRequestToProduct(productRequest);

        // Establish bidirectional relationship between seller and product
        seller.getProductList().add(product);
        product.setSeller(seller);

        // Persist seller (product will be saved via cascading if configured)
        sellerRepository.save(seller);

        // Return response DTO
        return ProductTransformer.productToProductResponse(product);
    }

    public List<ProductResponse> getProductByCategory(Category category) {

        // Fetch products by category
        return productRepository.findByCategory(category)
                .stream()
                // Convert Product entity to response DTO
                .map(ProductTransformer::productToProductResponse)
                .toList();
    }
}
