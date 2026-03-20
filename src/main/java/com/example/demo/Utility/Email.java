package com.example.demo.Utility;

import com.example.demo.dto.response.AddressResponse;
import com.example.demo.dto.response.OrderEntityResponse;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.model.OrderEntity;
import com.example.demo.model.Seller;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.transformers.AddressTransformer;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class Email {

    private final JavaMailSender javaMailSender;
    private final Validation validation;

    public void sendEmailAtCustomerRegistration(Customer customer) {

        String name = customer.getName();
        String email = customer.getEmail();
        String phone = customer.getPhonenumber();

        String htmlContent = """
    <html>
    <body style="font-family:Arial, Helvetica, sans-serif;background:#f4f6f8;padding:20px;">
    
    <div style="max-width:600px;margin:auto;background:#ffffff;border-radius:10px;
                overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.08);">

        <div style="background:#2874f0;color:white;padding:22px;text-align:center;">
            <h2 style="margin:0;">Welcome to ShopSphere 🎉</h2>
            <p style="margin:6px 0 0 0;font-size:14px;">Your shopping journey starts now</p>
        </div>

        <div style="padding:25px;line-height:1.6;color:#333;">

            <p>Dear <b>%s</b>,</p>

            <p>
            Your customer account has been successfully created on <b>ShopSphere</b>.
            You can now explore products, place orders, and manage your profile.
            </p>

            <h3 style="margin-top:20px;">Account Details</h3>

            <table style="border-collapse:collapse;width:100%%;margin-top:10px;">
                <tr style="background:#f5f5f5;">
                    <td style="padding:10px;border:1px solid #ddd;"><b>Registered Email</b></td>
                    <td style="padding:10px;border:1px solid #ddd;">%s</td>
                </tr>
                <tr>
                    <td style="padding:10px;border:1px solid #ddd;"><b>Phone Number</b></td>
                    <td style="padding:10px;border:1px solid #ddd;">%s</td>
                </tr>
            </table>

            <div style="text-align:center;margin-top:30px;">
                <a href="http://localhost:3000/login"
                   style="background:#ff9f00;color:white;padding:12px 22px;
                          text-decoration:none;border-radius:6px;font-weight:bold;">
                   Start Shopping
                </a>
            </div>

            <p style="margin-top:25px;">
            If you did not create this account or believe this registration was made in error,
            please contact our support team immediately.
            </p>

            <p style="margin-top:20px;">
            Best regards,<br>
            <b>ShopSphere Team</b>
            </p>

        </div>

        <div style="background:#f4f4f4;padding:15px;text-align:center;font-size:12px;color:#666;">
            © 2026 ShopSphere Inc. All rights reserved.<br>
            support@shopsphere.com
        </div>

    </div>

    </body>
    </html>
    """.formatted(name, email, phone);

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("dhruvjavadev162@gmail.com");
            helper.setTo(email);
            helper.setSubject("Welcome to ShopSphere – Account Created 🎉");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send registration email", e);
        }
    }

    public void sendEmailAtSellerRegistration(Seller savedSeller) {

        String sellerName = savedSeller.getName();
        String email = savedSeller.getEmail();

        String htmlContent = """
    <html>
    <body style="font-family:Arial, Helvetica, sans-serif;background:#f4f6f8;padding:20px;">
    
    <div style="max-width:600px;margin:auto;background:#ffffff;border-radius:10px;overflow:hidden;
                box-shadow:0 2px 8px rgba(0,0,0,0.08);">

        <div style="background:#2874f0;color:white;padding:22px;text-align:center;">
            <h2 style="margin:0;">Welcome to ShopSphere 🎉</h2>
            <p style="margin:6px 0 0 0;font-size:14px;">Your Seller Journey Starts Here</p>
        </div>

        <div style="padding:25px;line-height:1.6;color:#333;">

            <p>Dear <b>%s</b>,</p>

            <p>
            Your seller account has been successfully created on <b>ShopSphere</b>.
            You can now start listing products and managing orders from your dashboard.
            </p>

            <h3 style="margin-top:20px;">Account Details</h3>

            <table style="border-collapse:collapse;width:100%%;margin-top:10px;">
                <tr style="background:#f5f5f5;">
                    <td style="padding:10px;border:1px solid #ddd;"><b>Seller Name</b></td>
                    <td style="padding:10px;border:1px solid #ddd;">%s</td>
                </tr>
                <tr>
                    <td style="padding:10px;border:1px solid #ddd;"><b>Registered Email</b></td>
                    <td style="padding:10px;border:1px solid #ddd;">%s</td>
                </tr>
            </table>

            <h3 style="margin-top:25px;">Next Steps</h3>

            <ul style="padding-left:18px;">
                <li>Log in to your seller dashboard</li>
                <li>Complete your seller profile</li>
                <li>Add products to your catalog</li>
                <li>Start managing orders</li>
            </ul>

            <div style="text-align:center;margin-top:30px;">
                <a href="http://localhost:3000/seller/dashboard"
                   style="background:#ff9f00;color:white;padding:12px 22px;
                          text-decoration:none;border-radius:6px;font-weight:bold;">
                   Go to Seller Dashboard
                </a>
            </div>

            <p style="margin-top:25px;">
            If you did not create this account, please contact our support team immediately.
            </p>

            <p style="margin-top:20px;">
            Best regards,<br>
            <b>ShopSphere Team</b>
            </p>

        </div>

        <div style="background:#f4f4f4;padding:15px;text-align:center;font-size:12px;color:#666;">
            © 2026 ShopSphere Inc. All rights reserved.<br>
            support@shopsphere.com
        </div>

    </div>

    </body>
    </html>
    """.formatted(sellerName, sellerName, email);

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("dhruvjavadev162@gmail.com");
            helper.setTo(email);
            helper.setSubject("Welcome to ShopSphere – Seller Account Created 🎉");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send seller registration email", e);
        }
    }

    public void sendEmailAfterOrderPlaced(OrderEntity order) {

        if (order == null || order.getCustomer() == null) {
            throw new IllegalArgumentException("Order or customer cannot be null");
        }

        String email = order.getCustomer().getEmail();
        Customer customer = validation.checkCustomerByEmail_ReturnCustomer(email);

        String customerName = customer.getName();
        String phone = customer.getPhonenumber();

        String paymentStatus = order.getPaymentStatus().toString();
        String orderStatus = order.getOrderStatus().toString();

        Address address = order.getAddress();
        String deliveryAddress = address.getHouseno()+" "+address.getCity()+" "+address.getPinCode();

        LocalDateTime createdAt = order.getCreatedAt();
        String formattedDate = createdAt.format(DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm:ss"));

        BigDecimal totalAmount = order.getTotalAmount();

        // Order items table
        StringBuilder itemsBuilder = new StringBuilder();

        order.getOrderItems().forEach(item -> {
            itemsBuilder.append("""
            <tr>
                <td style="padding:8px;border:1px solid #ddd;">%s</td>
                <td style="padding:8px;border:1px solid #ddd;text-align:center;">%d</td>
                <td style="padding:8px;border:1px solid #ddd;text-align:right;">₹%s</td>
            </tr>
        """.formatted(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPrice()
            ));
        });

        String htmlContent = """
        <html>
        <body style="font-family:Arial,sans-serif;background:#f4f6f8;padding:20px;">
        
        <div style="max-width:600px;margin:auto;background:white;border-radius:10px;overflow:hidden;">
        
        <div style="background:#2874f0;color:white;padding:20px;text-align:center;">
            <h2>Order Confirmed 🎉</h2>
        </div>
        
        <div style="padding:25px;">
        
        <p>Dear <b>%s</b>,</p>
        
        <p>Thank you for your purchase. Your order has been successfully placed.</p>
        
        <h3>Order Details</h3>
        
        <p>
        <b>Phone:</b> %s <br>
        <b>Order Date:</b> %s <br>
        <b>Order Status:</b> %s <br>
        <b>Payment Status:</b> %s
        </p>
        
        <h3>Shipping Address</h3>
        <p>%s</p>
        
        <h3>Items Ordered</h3>
        
        <table style="border-collapse:collapse;width:100%%;">
            <tr style="background:#f2f2f2;">
                <th style="padding:10px;border:1px solid #ddd;">Product</th>
                <th style="padding:10px;border:1px solid #ddd;">Qty</th>
                <th style="padding:10px;border:1px solid #ddd;">Price</th>
            </tr>
            %s
        </table>
        
        <h2 style="text-align:right;margin-top:20px;">Total: ₹%s</h2>
        
        <div style="text-align:center;margin-top:30px;">
            <a href="http://localhost:3000/orders"
               style="background:#ff9f00;color:white;padding:12px 20px;
               text-decoration:none;border-radius:5px;font-weight:bold;">
               Track Your Order
            </a>
        </div>
        
        <p style="margin-top:30px;">We will notify you once your order ships.</p>
        
        <p>Thank you for shopping with us.</p>
        
        </div>
        
        <div style="background:#f4f4f4;padding:15px;text-align:center;font-size:12px;">
            © 2026 ShopSphere Inc.
        </div>
        
        </div>
        
        </body>
        </html>
        """.formatted(
                customerName,
                phone,
                formattedDate,
                orderStatus,
                paymentStatus,
                deliveryAddress,
                itemsBuilder.toString(),
                totalAmount
        );

        try {

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setFrom("dhruvjavadev162@gmail.com");
            helper.setSubject("Your Order is Confirmed 🎉");
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendEmailForOrderRefund(OrderEntity order) {

            if (order == null || order.getCustomer() == null) {
                throw new IllegalArgumentException("Order or customer cannot be null");
            }

            Customer customer = order.getCustomer();

            String customerName = customer.getName();
            String email = customer.getEmail();

            BigDecimal refundAmount = order.getTotalAmount();

            LocalDateTime createdAt = order.getCreatedAt();
            String formattedDate = createdAt.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            );

            Address address = order.getAddress();
            String deliveryAddress =
                    address.getHouseno() + " " +
                            address.getCity() + " " +
                            address.getPinCode();

            String htmlContent = """
    <html>
    <body style="font-family:Arial,sans-serif;background:#f4f6f8;padding:20px;">
    
    <div style="max-width:600px;margin:auto;background:white;border-radius:10px;overflow:hidden;">
    
    <div style="background:#d9534f;color:white;padding:20px;text-align:center;">
        <h2>Refund Processed</h2>
    </div>
    
    <div style="padding:25px;">
    
    <p>Dear <b>%s</b>,</p>
    
    <p>
    We would like to inform you that your refund has been successfully initiated.
    The amount will be credited back to your original payment method.
    </p>
    
    <h3>Refund Details</h3>
    
    <p>
    <b>Order Date:</b> %s <br>
    <b>Delivery Address:</b> %s <br>
    <b>Refund Amount:</b> ₹%s
    </p>
    
    <p>
    Refunds typically take <b>3–7 business days</b> to reflect in your account
    depending on your payment provider.
    </p>
    
    <div style="text-align:center;margin-top:30px;">
        <a href="http://localhost:3000/orders"
           style="background:#2874f0;color:white;padding:12px 20px;
           text-decoration:none;border-radius:5px;font-weight:bold;">
           View Your Orders
        </a>
    </div>
    
    <p style="margin-top:30px;">
    If you have any questions, please contact our support team.
    </p>
    
    <p>
    Best regards,<br>
    <b>ShopSphere Team</b>
    </p>
    
    </div>
    
    <div style="background:#f4f4f4;padding:15px;text-align:center;font-size:12px;">
        © 2026 ShopSphere Inc.
    </div>
    
    </div>
    
    </body>
    </html>
    """.formatted(
                    customerName,
                    formattedDate,
                    deliveryAddress,
                    refundAmount
            );

            try {

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setTo(email);
                helper.setFrom("dhruvjavadev162@gmail.com");
                helper.setSubject("Your Refund Has Been Initiated");
                helper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);

            } catch (Exception e) {
                throw new RuntimeException("Failed to send refund email", e);
            }
    }

    public void sendEmailForOrderCancellation(OrderEntity order) {

        if (order == null || order.getCustomer() == null) {
            throw new IllegalArgumentException("Order or Customer can't be null");
        }

        try {
            Customer customer = order.getCustomer();

            String customerName = customer.getName();
            String customerEmail = customer.getEmail();
            Integer orderId = order.getId();

            // Date formatting
            String orderDate = order.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            // Address
            Address address = order.getAddress();
            String deliveryAddress = address.getHouseno() + " "
                    + address.getCity() + " "
                    + address.getPinCode();

            // Amount (if needed for refund reference)
            BigDecimal amount = order.getTotalAmount();

            String htmlContent = """
        <html>
        <body style="font-family:Arial,sans-serif;background:#f4f6f8;padding:20px;">
        
        <div style="max-width:600px;margin:auto;background:white;border-radius:10px;overflow:hidden;">
        
        <div style="background:#6c757d;color:white;padding:20px;text-align:center;">
            <h2>Order Cancelled</h2>
        </div>
        
        <div style="padding:25px;">
        
        <p>Hi <b>%s</b>,</p>
        
        <p>Your order has been successfully cancelled. Below are the details:</p>
        
        <h3>Order Details</h3>
        
        <p>
        <b>Order ID:</b> %s <br>
        <b>Order Date:</b> %s <br>
        <b>Delivery Address:</b> %s <br>
        <b>Total Amount:</b> ₹%s
        </p>
        
        <p>
        If a payment was made, the refund will be processed to your original payment method
        within <b>3–7 business days</b>.
        </p>
        
        <div style="text-align:center;margin-top:30px;">
            <a href="http://localhost:3000/orders"
               style="background:#2874f0;color:white;padding:12px 20px;
               text-decoration:none;border-radius:5px;font-weight:bold;">
               View Your Orders
            </a>
        </div>
        
        <p style="margin-top:30px;">
        If you have any questions, feel free to contact our support team.
        </p>
        
        <p>
        Thanks,<br>
        <b>ShopSphere Team</b>
        </p>
        
        </div>
        
        <div style="background:#f4f4f4;padding:15px;text-align:center;font-size:12px;">
            © 2026 ShopSphere Inc.
        </div>
        
        </div>
        
        </body>
        </html>
        """.formatted(
                    customerName,
                    orderId,
                    orderDate,
                    deliveryAddress,
                    amount
            );

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(customerEmail);
            helper.setFrom("dhruvjavadev162@gmail.com");
            helper.setSubject("Your Order Has Been Cancelled");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send cancellation email", e);
        }
    }
}
