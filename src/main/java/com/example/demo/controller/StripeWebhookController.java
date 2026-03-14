package com.example.demo.controller;

import com.example.demo.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.model.Event;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class StripeWebhookController {

    private final OrderService orderService;

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

            orderService.markOrderDone(orderId);

            System.out.println("Order updated: " + orderId);
        }

        return ResponseEntity.ok("received");
    }
}