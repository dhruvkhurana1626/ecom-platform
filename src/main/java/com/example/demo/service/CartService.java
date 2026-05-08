package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.security.utility.SecurityUtil;
import com.example.demo.transformers.CartTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final Validation validation;

    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        Product product = validation.checkProductByProductId_ReturnProduct(request.getProductId());

        if (request.getQuantity() <= 0) {
            throw new InvalidRequestException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        int finalQuantity = request.getQuantity();

        if (existingItem != null) {
            finalQuantity += existingItem.getQuantity();
        }

        if (finalQuantity > product.getStock()) {
            throw new InvalidRequestException("Out of stock. Only " + product.getStock() + " available");
        }

        if (existingItem != null) {
            existingItem.setQuantity(finalQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(finalQuantity);
            newItem.setCart(cart);
            cart.getCartItems().add(newItem);
        }

        return CartTransformer.cartToCartResponse(cart);
    }

    @Transactional
    public CartResponse getCart() {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);
        Cart cart = validation.checkCartByCustomerId_ReturnCart(customer.getId());

        return CartTransformer.cartToCartResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(Integer productId) {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);
        Cart cart = validation.checkCartByCustomerId_ReturnCart(customer.getId());

        if(cart==null){
            throw new InvalidRequestException("Cart Already Empty");
        }

        boolean removed = cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        if (!removed) {throw new InvalidRequestException("Product not found in cart");}

        return CartTransformer.cartToCartResponse(cart);
    }

    @Transactional
    public void clearCart() {

        String email = SecurityUtil.getCurrentUserEmail();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);
        Cart cart = validation.checkCartByCustomerId_ReturnCart(customer.getId());

        if (cart == null || cart.getCartItems().isEmpty()) {
            return; // already empty
        }

        cart.getCartItems().clear();
    }
}
