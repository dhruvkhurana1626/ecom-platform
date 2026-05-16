package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.enums.Category;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.security.utility.SecurityUtil;
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
    public ProductResponse addProduct(ProductRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

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

    public ProductResponse getProductById(int productId) {

        //Fetch product by Id
        Product product = validation.checkProductByProductId_ReturnProduct(productId);
        return ProductTransformer.productToProductResponse(product);
    }

    public ProductResponse updateProductByProductId(ProductRequest productRequest,int productId) {

        String email = SecurityUtil.getCurrentUserEmail();

        Seller seller = validation.checkSellerByEmail_ReturnSeller(email);

        Product product = validation.checkProductByProductId_ReturnProduct(productId);

        if(!product.getSeller().getId().equals(seller.getId())){
            throw new InvalidRequestException("You don't have authority to perform this action");
        }

        if(productRequest.getName()!=null)
            product.setName(productRequest.getName());
        if(productRequest.getPrice()!=null)
            product.setPrice(productRequest.getPrice());
        if(productRequest.getStock()!=null)
            product.setStock(productRequest.getStock());
        if(productRequest.getCategory()!=null)
            product.setCategory(productRequest.getCategory());

        return ProductTransformer
                .productToProductResponse(productRepository
                        .save(product));
    }

    public void deleteProductById(int productId) {

        String email = SecurityUtil.getCurrentUserEmail();

        Seller seller = validation.checkSellerByEmail_ReturnSeller(email);

        Product product = validation.checkProductByProductId_ReturnProduct(productId);

        if(!product.getSeller().getEmail().equals(email)){
            throw new InvalidRequestException("Sorry you don't have the authority to perform this action");
        }

        productRepository.delete(product);
    }
}
