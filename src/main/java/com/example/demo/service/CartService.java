package com.example.demo.service;

import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.time.LocalDateTime;
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

    public void addToCart(AddToCartRequest addToCartRequest) {

        //Get logged in Customer
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        //Validation
        Customer customer = validation.checkIfCustomerExistByEmail_ReturnCustomer(email);
        Product product = validation.checkIfProductExist(addToCartRequest.getProductId());

        if(addToCartRequest.getQuantity()<=0){
            throw new InvalidRequestException("Quantity should be greater than 0");
        }

        //Fetching Cart
        Cart cart = cartRepository.findById(customer.getId()).
                orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });


        //Fetching the required Quantity
        int finalQuantity = addToCartRequest.getQuantity();

        //Checking if item available in cart
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(addToCartRequest.getProductId()))
                .findFirst()
                .orElseGet(null);

        if(cartItem!=null)finalQuantity += cartItem.getQuantity();
        if(finalQuantity > product.getQuantity()){
            throw new InvalidRequestException("Out of Stock , Only " + product.getQuantity() +" available");
        }

        if(cartItem!=null){
            cartItem.setQuantity(finalQuantity);
        }
        else{
            CartItem newCartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(finalQuantity);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
    }
}
