package com.example.demo.service;

import com.example.demo.Utility.Email;
import com.example.demo.Utility.Validation;
import com.example.demo.controller.stripe.StripeService;
import com.example.demo.dto.request.OrderEntityRequest;
import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderEntityRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.transformers.OrderTransformer;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final Validation validation;
    private final Email email;
    private final OrderEntityRepository orderEntityRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final StripeService stripeService;

    @Transactional
    public OrderEntityResponse placeOrder(OrderEntityRequest request) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);
        Cart cart = validation.checkCartByCustomerId_ReturnCart(customer.getId());

        if(customer.getAddresses().size()==0){
            throw new BusinessException("Pls add atleast 1 address before making an order");
        }

        if (cart.getCartItems().isEmpty()) {throw new BusinessException("Cart is empty");}

        if(orderEntityRepository.existsByCustomerAndPaymentStatus(
                customer,
                PaymentStatus.PENDING
        )){
            throw new BusinessException(
                    "Complete payment for your previous order first");
        }

        Address address = validation.checkAddressOwnership(request.getAddressId(), customer);

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);

        List<OrderItems> orderItemsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {

            int updated = productRepository.reduceStockIfAvailable(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity()
            );

            if (updated == 0) {
                throw new BusinessException(
                        cartItem.getProduct().getName() + " out of stock");
            }

            Product product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getStock()) {
                throw new BusinessException(product.getName() + " out of stock");
            }

            BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subTotal);

            OrderItems orderItem = new OrderItems();
            orderItem.setProduct(product);

            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrderEntity(order);

            orderItemsList.add(orderItem);
        }

        order.setOrderItems(orderItemsList);
        order.setTotalAmount(totalAmount);
        order.setAddress(address);
        order.setCurrency("inr");

        OrderEntity savedOrder = orderEntityRepository.save(order);

        // Stripe SessionIntent
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/success")
                        .setCancelUrl("http://localhost:3000/cancel")
                        .putMetadata("orderId", savedOrder.getId().toString());

        for (OrderItems item : orderItemsList) {

            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("inr")
                                            .setUnitAmount(
                                                    item.getPrice()
                                                            .multiply(BigDecimal.valueOf(100))
                                                            .longValue()
                                            )
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData
                                                            .builder()
                                                            .setName(item.getProduct().getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        try {

            Session session = Session.create(paramsBuilder.build());

            savedOrder.setStripeSessionId(session.getId());
            orderEntityRepository.save(savedOrder);

            OrderEntityResponse response =
                    OrderTransformer.orderEntityToOrderEntityResponse(savedOrder);

            response.setSetCheckoutUrl(session.getUrl());

            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage());
        }


//        // Stripe PaymentIntent
//        PaymentIntentCreateParams params =
//                PaymentIntentCreateParams.builder()
//                        .setAmount(
//                                totalAmount
//                                        .multiply(BigDecimal.valueOf(100))
//                                        .longValue()
//                        )
//                        .setCurrency("inr")
//                        .setAutomaticPaymentMethods(
//                                PaymentIntentCreateParams
//                                        .AutomaticPaymentMethods
//                                        .builder()
//                                        .setEnabled(true)
//                                        .build()
//                        )
//                        .putMetadata("orderId",
//                                savedOrder.getId().toString())
//                        .build();
//
//        try {
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//            savedOrder.setPaymentIntentId(paymentIntent.getId());
//            return OrderTransformer.orderEntityToOrderEntityResponse(savedOrder);
//
//        } catch (StripeException e) {
//            throw new RuntimeException("Stripe error: " + e.getMessage());
//        }
    }

    @Transactional
    public void restoreStock(OrderEntity order) {
        for (OrderItems item : order.getOrderItems()) {
            productRepository.incrementStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }
    }

    @Transactional
    public void cancelOrder(Integer orderId) {
        OrderEntity orderEntity = validation.checkOrderByOrderId_ReturnOrder(orderId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        if(!orderEntity.getCustomer().getId().equals(customer.getId())){
            throw new BusinessException("You don't have authority to cancel this order");
        }

        if(orderEntity.getOrderStatus()==OrderStatus.CANCELLED ||
                orderEntity.getOrderStatus()==OrderStatus.FAILED||
                orderEntity.getOrderStatus()==OrderStatus.REFUNDED){
            throw new InvalidRequestException("Order already closed");
        }

        //10 Minutes Cancellation Window
        long minutes = Duration.between(
                orderEntity.getCreatedAt(),
                LocalDateTime.now()).toMinutes();

        if(minutes>30){
            throw new InvalidRequestException(
                    "Cancellation window expired");
        }

        //Payment Still pending
        if(orderEntity.getPaymentStatus()==PaymentStatus.PENDING){
            orderEntity.setOrderStatus(OrderStatus.CANCELLED);
            orderEntity.setPaymentStatus(PaymentStatus.CANCELLED);
            restoreStock(orderEntity);
            return;
        }

        //Payment Already Successfull
        if(orderEntity.getPaymentStatus()==PaymentStatus.SUCCESS){
            orderEntity.setOrderStatus(OrderStatus.REFUND_INITIATED);
            orderEntity.setPaymentStatus(PaymentStatus.REFUND_INITIATED);

            //Stripe Api Integration
            OrderEntity order = validation.checkOrderByOrderId_ReturnOrder(orderId);
            stripeService.refundPayment(order.getPaymentIntentId());

            orderEntity.setOrderStatus(OrderStatus.REFUNDED);
            orderEntity.setPaymentStatus(PaymentStatus.REFUNDED);

            restoreStock(orderEntity);
        }
    }

    @Transactional
    public void markOrderDone(Integer orderId){
        OrderEntity order = validation.checkOrderByOrderId_ReturnOrder(orderId);

        order.setPaymentStatus(PaymentStatus.SUCCESS);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        email.sendEmailAfterOrderPlaced(order);
    }

    @Transactional
    public void markOrderRefunded(OrderEntity order) {
        order.setOrderStatus(OrderStatus.REFUNDED);
        order.setPaymentStatus(PaymentStatus.REFUNDED);

        email.sendEmailForOrderRefund(order);
    }
}
