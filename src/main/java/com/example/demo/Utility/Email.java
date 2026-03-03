package com.example.demo.Utility;

import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Email {

    private final JavaMailSender javaMailSender;

    public void sendEmailAtCustomerRegistration(Customer customer){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dhruvjavadev162@gmail.com");
        message.setTo(customer.getEmail());
        message.setSubject("Thanks for Creating an Account with E-Commerce Project");
        message.setText(
                "Hello " + customer.getName() + ",\n\n" +
                        "Welcome to the Java Backend Ecommerce Project.\n\n" +
                        "Your account has been successfully created. We kindly request you to " +
                        "confirm your registered phone number (" + customer.getPhonenumber() + ") " +
                        "to ensure uninterrupted access to our services.\n\n" +
                        "You are now free to explore and test the available APIs, including customer, " +
                        "product, order, and review features.\n\n" +
                        "If you have any questions or face any issues, feel free to reach out.\n\n" +
                        "Best regards,\n" +
                        "Dhruv Khurana\n" +
                        "Backend Engineer\n" +
                        "Java | Spring Boot"
        );

        javaMailSender.send(message);
    }

//    public void sendEmailAfterOrderPlaced(OrderEntityResponse orderEntityResponse) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("dhruvjavadev162@gmail.com");
//        message.setTo(orderEntityResponse.getCustomerResponse().getEmail());
//        message.setSubject("Order Confirmed | Order ID: " + orderEntityResponse.getId());
//
//        StringBuilder body = new StringBuilder();
//
//        body.append("Hello ")
//                .append(orderEntityResponse.getCustomerResponse().getName())
//                .append(",\n\n");
//
//        body.append("Thank you for placing your order with us. ")
//                .append("Your order has been successfully placed and is currently in ")
//                .append(orderEntityResponse.getOrderStatus())
//                .append(" status.\n\n");
//
//        body.append("Order Details:\n");
//        body.append("Order ID   : ").append(orderEntityResponse.getId()).append("\n");
//        body.append("Order Date : ").append(orderEntityResponse.getCreatedAt()).append("\n\n");
//
//        body.append("Items Ordered:\n");
//
//        orderEntityResponse.getOrderItemsResponse().forEach(item -> {
//            body.append("- ")
//                    .append(item.getProductResponse().getName())
//                    .append(" | Qty: ").append(item.getQuantiy())
//                    .append(" | Price: ₹").append(item.getProductResponse().getPrice()*item.getQuantiy())
//                    .append("\n");
//        });
//
//        body.append("\nTotal Amount Paid: ₹")
//                .append(orderEntityResponse.getTotalCost())
//                .append("\n\n");
//
//        body.append("If you have any questions regarding your order, ")
//                .append("feel free to contact our support team.\n\n");
//
//        body.append("We hope you enjoy your experience with us.\n\n");
//        body.append("Best Regards,\n");
//        body.append("Swiggato Team\n");
//        body.append("— Powered by Java Backend");
//
//        message.setText(body.toString());
//
//        javaMailSender.send(message);
//    }

}
