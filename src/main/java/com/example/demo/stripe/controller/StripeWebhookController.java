package com.example.demo.stripe.controller;

import com.example.demo.Utility.Validation;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.OrderEntity;
import com.example.demo.repository.OrderEntityRepository;
import com.example.demo.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.Event;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class StripeWebhookController {

    private final OrderService orderService;
    private final Validation validation;
    private final OrderEntityRepository orderEntityRepository;

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {

            Session session = (Session) event
                    .getDataObjectDeserializer()
                    .deserializeUnsafe();

            if (session == null) {
                System.out.println("Session object could not be parsed");
                return ResponseEntity.ok("session null");
            }

            String orderIdStr = session.getMetadata().get("orderId");
            Integer orderId = Integer.valueOf(orderIdStr);
            String paymentIntentId = session.getPaymentIntent();

            orderService.markOrderDone(orderId);

            OrderEntity order = validation.checkOrderByOrderId_ReturnOrder(orderId);
            order.setPaymentIntentId(paymentIntentId);
            orderEntityRepository.save(order);
        }

        if("checkout.session.expired".equals(event.getType())){

            Session session = (Session) event
                    .getDataObjectDeserializer()
                    .deserializeUnsafe();

            if (session == null) {
                System.out.println("Session object could not be parsed");
                return ResponseEntity.ok("session null");
            }

            String orderIdStr = session.getMetadata().get("orderId");
            Integer orderId = Integer.valueOf(orderIdStr);

            OrderEntity order = validation.checkOrderByOrderId_ReturnOrder(orderId);

            orderService.cancelOrder(orderId);
        }

        if ("charge.refunded".equals(event.getType())) {

            Charge charge = (Charge) event
                    .getDataObjectDeserializer()
                    .deserializeUnsafe();

            String paymentIntentId = charge.getPaymentIntent();

            if(paymentIntentId==null){
                throw new BusinessException("No History of Order Found");
            }

            OrderEntity order = validation.findByPaymentIntentId_ReturnOrder(paymentIntentId);

            if(order.getPaymentStatus() != PaymentStatus.REFUNDED){

                orderService.markOrderRefunded(order);
            }
        }

        return ResponseEntity.ok("received");
    }
}