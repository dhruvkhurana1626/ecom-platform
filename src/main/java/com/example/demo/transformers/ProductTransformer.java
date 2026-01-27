package com.example.demo.transformers;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.model.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ProductTransformer {

    public static Product productRequestToProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .build();

        return product;
    }

    public static ProductResponse productToProductResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .sellerResponse(SellerTransformer.sellerToSellerResponse(product.getSeller()))
                .build();

        return productResponse;
    }

    public static List<ProductResponse> productListToProductResponse(List<Product> productList){
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product product : productList){
            productResponseList.add(ProductTransformer.productToProductResponse(product));
        }
        return productResponseList;
    }
}
