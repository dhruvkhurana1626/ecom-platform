package com.example.demo.service;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.dto.response.SellerResponse;
import com.example.demo.exception.SellerNotFound;
import com.example.demo.model.Product;
import com.example.demo.model.Seller;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.transformers.ProductTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SellerRepository sellerRepository;

    public ProductResponse addProduct(ProductRequest productRequest, int id) {
        Optional<Seller> optionalSeller = sellerRepository.findById(id);
        if(optionalSeller.isEmpty()){
            throw new SellerNotFound("Invalid Id");
        }

        Seller seller = optionalSeller.get();
        Product product = ProductTransformer.productRequestToProduct(productRequest);
        seller.getProductList().add(product);

        product.setSeller(seller);

        sellerRepository.save(seller);

        ProductResponse productResponse = ProductTransformer.productToProductResponse(product);
        return productResponse;
    }
}
