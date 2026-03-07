package com.example.demo.Utility;

import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Email {

    private final JavaMailSender javaMailSender;

    public void sendEmailAtCustomerRegistration(Customer customer) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("dhruvjavadev162@gmail.com");
        message.setTo(customer.getEmail());
        message.setSubject("Welcome to ShopSphere – Account Created Successfully");

        message.setText(
                "Dear " + customer.getName() + ",\n\n" +

                        "Welcome to ShopSphere.\n\n" +

                        "Your customer account has been successfully created with the following details:\n" +
                        "Registered Email: " + customer.getEmail() + "\n" +
                        "Phone Number: " + customer.getPhonenumber() + "\n\n" +

                        "You can now log in to your account and start exploring products, " +
                        "placing orders, and managing your profile.\n\n" +

                        "If you did not create this account or believe this registration was made in error, " +
                        "please contact our support team immediately.\n\n" +

                        "Best regards,\n" +
                        "ShopSphere Team\n" +
                        "support@shopsphere.com"
        );

        javaMailSender.send(message);
    }

    public void sendEmailAtSellerRegistration(Seller savedSeller) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String subject = "Welcome to ShopSphere – Seller Account Successfully Created";
        String body = "Dear " + savedSeller.getName() + ",\n\n"
                + "Welcome to ShopSphere.\n\n"
                + "Your seller account has been successfully created with the following details:\n\n"
                + "Seller Name: " + savedSeller.getName() + "\n"
                + "Registered Email: " + savedSeller.getEmail() + "\n\n"
                + "You can now log in to the seller dashboard and start listing your products.\n\n"
                + "Next Steps:\n"
                + "1. Log in to your seller account.\n"
                + "2. Complete your profile details.\n"
                + "3. Add products to your catalog.\n"
                + "4. Start managing orders.\n\n"
                + "If you did not create this account, please contact our support team.\n\n"
                + "Best regards,\n"
                + "ShopSphere Team\n"
                + "support@shopsphere.com";

        simpleMailMessage.setFrom("dhruvjavadev162@gmail.com");
        simpleMailMessage.setTo(savedSeller.getEmail());
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
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
