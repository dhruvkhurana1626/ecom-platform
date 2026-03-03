package com.example.demo.service;

import com.example.demo.Utility.Email;
import com.example.demo.Utility.Validation;
import com.example.demo.dto.request.OrderItemRequest;
import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.InvalidRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.OrderItems;
import com.example.demo.model.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderEntityRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.transformers.OrderTransformer;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.enums.OrderStatus.PENDING_PAYMENT;

@Service
@RequiredArgsConstructor

public class OrderService {

    private final Validation validation;
    private final Email email;
    private final OrderEntityRepository orderEntityRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public OrderEntityResponse placeOrder(int customerId,
                                          List<OrderItemRequest> orderItemRequestList) {

        Customer customer = validation.checkIfCustomerExist(customerId);
        validation.validateOrderItemsList(orderItemRequestList);

        if (customer.getAddresses() == null) {
            throw new BusinessException("Please add a delivery address before placing order");
        }

        if(orderEntityRepository.existsByCustomerAndOrderStatus(customer, PENDING_PAYMENT))
            throw new BusinessException("Complete existing payment first");

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomer(customer);

        List<OrderItems> orderItemsList = new ArrayList<>();
        int totalCost = 0;

        for (OrderItemRequest orderItemRequest : orderItemRequestList) {

            int quantity = orderItemRequest.getQuantity();

            int updated = productRepository.reduceStock(
                    orderItemRequest.getProductId(),
                    quantity
            );

            if (updated == 0) {
                throw new BusinessException("Product out of stock");
            }

            Product product = productRepository.findById(orderItemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            int priceAtOrder = product.getPrice() * quantity;
            totalCost += priceAtOrder;

            OrderItems orderItems = new OrderItems();
            orderItems.setQuantity(quantity);
            orderItems.setPrice(priceAtOrder);
            orderItems.setProduct(product);

            orderItems.setOrderEntity(orderEntity);

            orderItemsList.add(orderItems);
        }

        orderEntity.setOrderItems(orderItemsList);
        orderEntity.setTotalCost(totalCost);
        orderEntity.setOrderStatus(PENDING_PAYMENT);

        OrderEntity savedOrderEntity = orderEntityRepository.save(orderEntity);

        OrderEntityResponse orderEntityResponse =
                OrderTransformer.orderEntityToOrderEntityResponse(savedOrderEntity);

        return orderEntityResponse;
    }

    @Transactional
    public void updateOrderAfterPayment(int orderId, boolean paymentSuccess) {

        OrderEntity order = orderEntityRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (paymentSuccess) {
            orderEntityRepository.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

            OrderEntityResponse response =
                    OrderTransformer.orderEntityToOrderEntityResponse(order);

            email.sendEmailAfterOrderPlaced(response);

        } else {
            order.setOrderStatus(OrderStatus.FAILED);

            // restore stock
            restoreStock(order);
        }
    }


    @Transactional
    public void cancelOrder(int orderId) {

        OrderEntity order = orderEntityRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer Not Found"));

        if(!customer.getId().equals(order.getCustomer().getId())){
            throw new BusinessException("You dont have the authority to cancel this order");
        }

        if (Duration.between(order.getCreatedAt(), LocalDateTime.now()).toMinutes() > 10) {
            throw new InvalidRequestException("Cancellation window expired");
        }

        // ! Rule 1: Cannot cancel if already cancelled or failed
        if (order.getOrderStatus() == OrderStatus.CANCELLED ||
                order.getOrderStatus() == OrderStatus.FAILED) {
            throw new InvalidRequestException("Order already closed");
        }

        // ! Rule 2: If payment pending → just cancel
        if (order.getOrderStatus() == PENDING_PAYMENT) {

            orderEntityRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            // restore stock
            restoreStock(order);
            return;
        }

        // ! Rule 3: If already confirmed → refund required
        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {

            orderEntityRepository.updateOrderStatus(orderId, OrderStatus.REFUND_INITIATED);

            // Here you would call Stripe refund API

            order.setOrderStatus(OrderStatus.REFUNDED);

            restoreStock(order);
        }
    }

    private void restoreStock(OrderEntity order) {
        for (OrderItems item : order.getOrderItems()) {
            Product product = item.getProduct();
            productRepository.increaseStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }
    }
}
