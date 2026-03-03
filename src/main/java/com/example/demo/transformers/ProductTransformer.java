package com.example.demo.transformers;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductTransformer {

    public static Product productRequestToProduct(ProductRequest request) {

        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())      // BigDecimal
                .stock(request.getStock())      // renamed from quantity
                .category(request.getCategory())
                .build();
    }

    public static ProductResponse productToProductResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .sellerName(product.getSeller().getName())
                .build();
    }
}
