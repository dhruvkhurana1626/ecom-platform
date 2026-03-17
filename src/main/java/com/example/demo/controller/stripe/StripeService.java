package com.example.demo.controller.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Transactional
    public void refundPayment(String paymentIntentId) {
        try {

            RefundCreateParams params =
                    RefundCreateParams.builder()
                            .setPaymentIntent(paymentIntentId)
                            .build();

            Refund refund = Refund.create(params);

            System.out.println("Refund created: " + refund.getId());

        } catch (StripeException e) {
            throw new RuntimeException("Refund failed: " + e.getMessage());
        }
    }
}
