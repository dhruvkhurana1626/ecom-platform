package com.example.demo.transformers;

import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartTransformer {

    public static CartResponse cartToCartResponse(Cart cart) {

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(convertCartToResponse(cart.getCartItems()))
                .totalAmount(totalAmountOfCartItems(cart.getCartItems()))
                .build();
    }

    public static List<CartItemResponse> convertCartToResponse(
            List<CartItem> cartItemsList) {

        return cartItemsList.stream()
                .map(cartItem -> {

                    BigDecimal price = cartItem.getProduct().getPrice();
                    BigDecimal quantity =
                            BigDecimal.valueOf(cartItem.getQuantity());

                    BigDecimal subTotal = price.multiply(quantity);

                    return CartItemResponse.builder()
                            .productId(cartItem.getProduct().getId())
                            .productName(cartItem.getProduct().getName())
                            .price(price)
                            .quantity(cartItem.getQuantity())
                            .subTotal(subTotal)
                            .build();
                })
                .toList();
    }

    public static BigDecimal totalAmountOfCartItems(
            List<CartItem> cartItemsList) {

        return cartItemsList.stream()
                .map(item ->
                        item.getProduct()
                                .getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
