package com.example.demo.transformers;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductTransformer {

    public static Product productRequestToProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .type(productRequest.getType())
                .build();

        return product;
    }

    public static ProductResponse productToProductResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .type(product.getType())
                .sellerResponse(SellerTransformer.sellerToSellerResponse(product.getSeller()))
                .build();

        return productResponse;
    }
}
