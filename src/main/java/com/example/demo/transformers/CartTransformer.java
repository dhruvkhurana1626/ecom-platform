package com.example.demo.transformers;

import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartTransformer {

    public static CartResponse cartToCartResponse(Cart cart){
        CartResponse cartResponse = CartResponse.builder()
                .cartItemResponseList(convertCartToResponse(cart.getCartItems()))
                .totalAmt(totalAmountOfCartItems(cart.getCartItems()))
                .build();

        return cartResponse;
    }

    public static List<CartItemResponse> convertCartToResponse(List<CartItem> cartItemsList){
        List<CartItemResponse> cartItemResponseList = new ArrayList<>();
        for(CartItem cartItem : cartItemsList){
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setProductId(cartItem.getProduct().getId());
            cartItemResponse.setProductName(cartItem.getProduct().getName());
            cartItemResponse.setPrice(cartItem.getProduct().getPrice());
            cartItemResponse.setQuantity(cartItem.getQuantity());
            cartItemResponse.setSubTotal(cartItem.getQuantity()*cartItem.getProduct().getPrice());
            cartItemResponseList.add(cartItemResponse);
        }
        return cartItemResponseList;
    }

    public static int totalAmountOfCartItems(List<CartItem> cartItemsList){
        int ans = 0;
        for(CartItem item : cartItemsList){
            ans += item.getProduct().getPrice()*item.getQuantity();
        }
        return ans;
    }


}
